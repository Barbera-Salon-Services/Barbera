package com.barbera.barberaconsumerapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.barbera.barberaconsumerapp.network_aws.JsonPlaceHolderApi2;
import com.barbera.barberaconsumerapp.network_aws.Register;
import com.barbera.barberaconsumerapp.network_aws.RetrofitClientInstanceUser;
import com.barbera.barberaconsumerapp.sms.SmsReceiver;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ActivityPhoneVerification extends AppCompatActivity implements LocationListener,
        OnOtpCompletionListener {
    private LocationManager locationManager;
    private TextView phoneNumberText;

    private OtpView phoneNumberOtpView, otpView;
    //    private CardView get_code;
    private TextView skipLogin, enterOtpTextView;
    private ProgressDialog progressDialog;
    private EditText ref;
    //    private CardView continue_to_signup;
    private String tempToken;
    private double lat;
    private double lon;
    private String phonePattern;
    private String phoneNumberValue, otpValue, fullAddress = "";
    private ImageView logoView, logoCenterView;
    private Handler mHandler1;
    private Runnable mRunnable1;

    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        phoneNumberOtpView = (OtpView) findViewById(R.id.phone);
        phoneNumberText = (TextView) findViewById(R.id.phone_number_text);
        skipLogin = findViewById(R.id.skip_login);

        enterOtpTextView = (TextView) findViewById(R.id.veri_code_textview);
        otpView = (OtpView) findViewById(R.id.veri_code);
//        ref = findViewById(R.id.referral_code);
//
//        ref.setSelection(ref.getText().length());
//
//        ref.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if(b){
//                    ref.setHint("");
//                }
//                else{
//                    ref.setHint("Enter referral code");
//                }
//            }
//        });

        phonePattern = "^[6789]\\d{9}$";
        progressDialog = new ProgressDialog(ActivityPhoneVerification.this);
        logoView = (ImageView) findViewById(R.id.logo);
        logoCenterView = (ImageView) findViewById(R.id.logo_center);


        skipLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });
        phoneNumberOtpView.setOtpCompletionListener(this);
        otpView.setOtpCompletionListener(this);
        phoneNumberText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumberText.setVisibility(View.GONE);
                phoneNumberOtpView.setVisibility(View.VISIBLE);
                phoneNumberOtpView.setFocusableInTouchMode(true);
                phoneNumberOtpView.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });
        enterOtpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterOtpTextView.setVisibility(View.GONE);
                otpView.setVisibility(View.VISIBLE);
                otpView.setFocusableInTouchMode(true);
                otpView.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                // Try reading sms
//                autoReadOTP();
            }
        });
        new SmsReceiver().setEditText(otpView);
        handleAnimation();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getLastLocation();
    }

    private void handleAnimation() {
        phoneNumberText.setVisibility(View.INVISIBLE);
//        ref.setVisibility(View.INVISIBLE);
        logoView.setVisibility(View.INVISIBLE);
        logoCenterView.setVisibility(View.VISIBLE);

        Animation animationSlideUp = AnimationUtils.loadAnimation(this, R.anim.slide_out);
        logoCenterView.startAnimation(animationSlideUp);
        mHandler1 = new Handler();
        mRunnable1 = new Runnable() {
            @Override
            public void run() {
                logoCenterView.setVisibility(View.GONE);
                phoneNumberText.setVisibility(View.VISIBLE);
//                ref.setVisibility(View.VISIBLE);
                logoView.setVisibility(View.VISIBLE);
            }
        };
        mHandler1.postDelayed(mRunnable1, 1500);
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            lat = location.getLatitude();
                            lon = location.getLongitude();

                            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                            List<Address> addressList = null;
                            Log.d("Location", "Not null");
                            try {
                                addressList = geocoder.getFromLocation(lat, lon, 1);
                                fullAddress = addressList.get(0).getAddressLine(0);
                                Log.d("TAG", fullAddress);
                            } catch (IOException e) {
                                e.printStackTrace();
                                fullAddress = "";
                            }
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            lat = mLastLocation.getLatitude();
            lon = mLastLocation.getLongitude();

            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addressList = null;
            try {
                addressList = geocoder.getFromLocation(lat, lon, 1);
                fullAddress = addressList.get(0).getAddressLine(0);
            } catch (IOException e) {
                e.printStackTrace();
                fullAddress = "";
            }

        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED;

    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.RECEIVE_SMS}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then

    @Override
    public void onOtpCompleted(String otp) {
        if (otp.length() == 6) {
            otpValue = otp;
            autoVerifyOTP();
        } else {
            phoneNumberValue = otp;
            fetchOtp();
        }
    }

    private void autoVerifyOTP() {
        if (verifyUserOTP()) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    verifyUser();
                }
            }, 1500);

        }
    }

    private void fetchOtp() {
        if (verifyPhoneNumber()) {
            //progressBar.setVisibility(View.VISIBLE);
            sendToastmsg("Sending OTP");
            sendfVerificationCode();
        }
    }

    private void verifyUser() {
        ProgressDialog progressDialog = new ProgressDialog(ActivityPhoneVerification.this);
        progressDialog.setMessage("Hold on for a moment...");
        progressDialog.show();
        Retrofit retrofit = RetrofitClientInstanceUser.getRetrofitInstance();
        JsonPlaceHolderApi2 jsonPlaceHolderApi2 = retrofit.create(JsonPlaceHolderApi2.class);

        Call<Register> call = jsonPlaceHolderApi2.checkOtp(new Register(null, otpValue, null, null, null,
                fullAddress, "user", null, lat, lon, ""), "Bearer " + tempToken);
//        ProgressDialog progressDialog=new ProgressDialog(ActivityPhoneVerification.this);
//        progressDialog.setMessage("Logging you in");
        call.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                if (response.code() == 200) {
                    SharedPreferences sharedPreferences1 = getSharedPreferences("Profile", MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                    editor1.putString("address", fullAddress);
                    editor1.putString("phone", phoneNumberValue);
                    editor1.apply();

                    Register register = response.body();
                    SharedPreferences sharedPreferences = getSharedPreferences("Token", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", register.getToken());
                    editor.apply();
                    FirebaseMessaging.getInstance().subscribeToTopic(phoneNumberValue).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });
                    progressDialog.dismiss();
                    //progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(ActivityPhoneVerification.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    //progressBar.setVisibility(View.GONE);
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Request not sent", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                //progressBar.setVisibility(View.GONE);
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean verifyUserOTP() {
        if (otpValue.length() != 6) {
            otpView.setError("Invalid OTP");
            otpView.requestFocus();
            return false;
        } else
            return true;
    }

    private boolean verifyPhoneNumber() {
        if (phoneNumberValue.matches(phonePattern))
            return true;
        else {
            phoneNumberOtpView.setError("Please Enter a valid Phone Number");
            phoneNumberOtpView.requestFocus();
            return false;
        }
    }

    private void sendfVerificationCode() {
        Retrofit retrofit = RetrofitClientInstanceUser.getRetrofitInstance();
//        ProgressDialog progressDialog=new ProgressDialog(ActivityPhoneVerification.this);
//        progressDialog.setMessage("Hold on for a moment...");
//        progressDialog.show();
        JsonPlaceHolderApi2 jsonPlaceHolderApi2 = retrofit.create(JsonPlaceHolderApi2.class);
        Call<Register> call = jsonPlaceHolderApi2.getToken(new Register(phoneNumberValue, null, null, null, null, null, null, null, 0.0, 0.0, null));
        call.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                if (response.code() == 200) {
                    Register register = response.body();
                    tempToken = register.getToken();
                    phoneNumberOtpView.setVisibility(View.GONE);
                    enterOtpTextView.setVisibility(View.VISIBLE);
                    otpView.setVisibility(View.GONE);
                    //progressDialog.dismiss();
                } else {
                    //progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Request not sent", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                //progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendToastmsg(String text) {
        Toast msg = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
        msg.show();
    }

    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.v("Location Changed", location.getLatitude() + " and " + location.getLongitude());
            locationManager.removeUpdates(this);
        }
    }

    // Required functions
    public void onProviderDisabled(String arg0) {
    }

    public void onProviderEnabled(String arg0) {
    }

    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 8080) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(getApplicationContext(), "Cannot fetch loaction without enabling location services", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        if (requestCode == 800) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    break;
                case Activity.RESULT_CANCELED:
                    finish();
                    startActivity(getIntent());
                    break;

            }

        }
    }

    private void displayNeverAskAgainDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("We need to send SMS for performing necessary task. Please permit the permission through "
                + "Settings screen.\n\nSelect Permissions -> Enable permission");
        builder.setCancelable(false);
        builder.setPositiveButton("Permit Manually", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "App will not work unless location permission is provided from settings", Toast.LENGTH_SHORT).show();
                //login.setEnabled(false);
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
        if (requestCode == 4) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                finish();
                startActivity(new Intent(this, ActivityPhoneVerification.class));
            } else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                displayNeverAskAgainDialog();

            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = getSharedPreferences("Token", MODE_PRIVATE);
        String isRegistered = preferences.getString("token", "no");
        if (!isRegistered.equals("no")) {
            startActivity(new Intent(this, HomeActivity.class));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler1.removeCallbacks(mRunnable1);
    }
}
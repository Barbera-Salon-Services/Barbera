package com.barbera.barberaconsumerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.barbera.barberaconsumerapp.network_aws.JsonPlaceHolderApi2;
import com.barbera.barberaconsumerapp.network_aws.Register;
import com.barbera.barberaconsumerapp.network_aws.RetrofitClientInstanceUser;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
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
    private Address address;
    private TextView phoneNumberText;
    private OtpView phoneNumberOtpView, otpView;
    private CardView get_code;
    private TextView skipLogin, enterOtpTextView;
    private ProgressDialog progressDialog;
    private EditText ref;
    private CardView continue_to_signup;
    private ProgressBar progressBar;
    private String tempToken;
    private String phonePattern;
    private String phoneNumberValue, otpValue;
    private Criteria criteria;
    private LocationListener locationListener;
    private LocationRequest locationRequest;
    private Looper looper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        phoneNumberOtpView = (OtpView) findViewById(R.id.phone);
        phoneNumberText = (TextView) findViewById(R.id.phone_number_text);
        skipLogin = findViewById(R.id.skip_login);
        get_code = (CardView) findViewById(R.id.get_code);
        enterOtpTextView = (TextView) findViewById(R.id.veri_code_textview);
//        veri_code = (EditText) findViewById(R.id.veri_code);
        otpView = (OtpView) findViewById(R.id.veri_code);
        continue_to_signup = findViewById(R.id.continue_to_signup_page);
        progressBar = findViewById(R.id.progressBarInVerificationPage);
        ref = findViewById(R.id.referral_code);
        phonePattern = "^[6789]\\d{9}$";
        progressDialog = new ProgressDialog(ActivityPhoneVerification.this);

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(ActivityPhoneVerification.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ActivityPhoneVerification.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 4);
        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> addressList = null;
                Log.d("Location", "Not null");
                try {
                    addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                address = addressList.get(0);
                Log.d("address", address.toString());
                Log.d("Location Changes", location.toString());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d("Status Changed", String.valueOf(status));
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d("Provider Enabled", provider);
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d("Provider Disabled", provider);
            }
        };

        // Now first make a criteria with your requirements
        // this is done to save the battery life of the device
        // there are various other other criteria you can search for..
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);

        // Now create a location manager
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // This is the Best And IMPORTANT part
        looper = null;

        get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchOtp();
            }
        });

        continue_to_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoVerifyOTP();
            }
        });
        skipLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });

        handlePhoneOtp();

    }

    private void handlePhoneOtp() {
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
            }
        });

    }

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
            continue_to_signup.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
            //PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationId,veri_code.getText().toString());
            //Toast.makeText(getApplicationContext(), "In", Toast.LENGTH_SHORT).show();
            verifyUser();
        }
    }

    private void fetchOtp() {
        if (verifyPhoneNumber()) {
            Log.d("onclick", "In");
            if (ActivityCompat.checkSelfPermission(ActivityPhoneVerification.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ActivityPhoneVerification.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ActivityPhoneVerification.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 4);
            } else {
                Log.d("permission", "given");
                if (isLocationEnabled()) {
                    Log.d("Enabled", "Yes");
                    locationManager.requestSingleUpdate(criteria, locationListener, looper);
                } else {
                    enableLocation(locationRequest);
                    finish();
                    startActivity(new Intent(ActivityPhoneVerification.this, ActivityPhoneVerification.class));
                }
            }
            get_code.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
            sendToastmsg("Sending OTP");
            sendfVerificationCode();
        }
    }

    private void verifyUser() {
        Retrofit retrofit = RetrofitClientInstanceUser.getRetrofitInstance();
        JsonPlaceHolderApi2 jsonPlaceHolderApi2 = retrofit.create(JsonPlaceHolderApi2.class);
        //Toast.makeText(getApplicationContext(), address.getAddressLine(0), Toast.LENGTH_SHORT).show();
        Call<Register> call = jsonPlaceHolderApi2.checkOtp(new Register(null, otpValue, null, null, null,
                address.getAddressLine(0), "user", null, address.getLatitude(), address.getLongitude(), ref.getText().toString()), "Bearer " + tempToken);
//        ProgressDialog progressDialog=new ProgressDialog(ActivityPhoneVerification.this);
//        progressDialog.setMessage("Logging you in");
        call.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                if (response.code() == 200) {
                    SharedPreferences sharedPreferences1 = getSharedPreferences("Profile", MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                    editor1.putString("address", address.getAddressLine(0));
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
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(ActivityPhoneVerification.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Request not sent", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
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

        JsonPlaceHolderApi2 jsonPlaceHolderApi2 = retrofit.create(JsonPlaceHolderApi2.class);
        Call<Register> call = jsonPlaceHolderApi2.getToken(new Register(phoneNumberValue, null, null, null, null, null, null, null, 0.0, 0.0, null));
        call.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                if (response.code() == 200) {
                    Register register = response.body();
                    tempToken = register.getToken();
                    progressBar.setVisibility(View.INVISIBLE);
                    enterOtpTextView.setVisibility(View.GONE);
                    otpView.setVisibility(View.VISIBLE);
                    continue_to_signup.setVisibility(View.VISIBLE);
                    get_code.setVisibility(View.GONE);
                    phoneNumberOtpView.setVisibility(View.GONE);
                    enterOtpTextView.setVisibility(View.VISIBLE);
                    otpView.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Request not sent", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
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

    private void enableLocation(LocationRequest locationRequest) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        Task<LocationSettingsResponse> task = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());
        task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    task.getResult(ApiException.class);
                } catch (ApiException e) {
                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(ActivityPhoneVerification.this, 8080);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        });
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

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
}
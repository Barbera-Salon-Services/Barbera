 package com.barbera.barberaconsumerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.barbera.barberaconsumerapp.network_aws.JsonPlaceHolderApi2;
import com.barbera.barberaconsumerapp.network_aws.Register;
import com.barbera.barberaconsumerapp.network_aws.RetrofitClientInstance2;
import com.barbera.barberaconsumerapp.network_email.RetrofitClientInstance;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ActivityPhoneVerification extends AppCompatActivity {
    private FusedLocationProviderClient client;
    private Location currentLocation;
    private Address address;
    private EditText phoneNumber;
    private CardView get_code;
    private ProgressDialog progressDialog;
    private EditText veri_code;
    private CardView continue_to_signup;
    private FirebaseAuth mauth;
    private ProgressBar progressBar;
    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken token;
    private FirebaseFirestore fstore;
    private DocumentReference documentReference;
    private String phonePattern;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        phoneNumber = (EditText) findViewById(R.id.phone);
        get_code = (CardView) findViewById(R.id.get_code);
        veri_code = (EditText) findViewById(R.id.veri_code);
        continue_to_signup = findViewById(R.id.continue_to_signup_page);
        mauth = FirebaseAuth.getInstance();
        progressBar= findViewById(R.id.progressBarInVerificationPage);
        fstore=FirebaseFirestore.getInstance();
        phonePattern="^[6789]\\d{9}$";
        progressDialog=new ProgressDialog(ActivityPhoneVerification.this);

        sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
        client = LocationServices.getFusedLocationProviderClient(this);

        @SuppressLint("MissingPermission") Task<Location> task =client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    Geocoder geocoder =  new Geocoder(getApplicationContext(), Locale.getDefault());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                        address = addressList.get(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verifyPhoneNumber()){
                    get_code.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                    sendToastmsg("Sending OTP");
                    sendfVerificationCode();
                }

            }
        });

        continue_to_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verifyUserOTP()){
                    continue_to_signup.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                    //PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationId,veri_code.getText().toString());
                    Toast.makeText(getApplicationContext(),"In",Toast.LENGTH_SHORT).show();
                    verifyUser();
                }

            }
        });
    }

    private void verifyUser() {
//        mauth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if(task.isSuccessful()){
//                    progressDialog.setMessage("Verification Completed...");
//                    progressDialog.show();
//                    progressDialog.setCancelable(false);
//                    whetherNewOrOldUser();
//                }
//                else{
//                    sendToastmsg(task.getException().getMessage());
//                    continue_to_signup.setEnabled(true);
//                    progressBar.setVisibility(View.INVISIBLE);
//                    get_code.setEnabled(true);
//                }
//            }
//        });
        Retrofit retrofit= RetrofitClientInstance2.getRetrofitInstance();
        JsonPlaceHolderApi2 jsonPlaceHolderApi2=retrofit.create(JsonPlaceHolderApi2.class);
        SharedPreferences sharedPreferences = getSharedPreferences("Token",MODE_PRIVATE);
        String token = sharedPreferences.getString("token",null);
        Call<Register> call= jsonPlaceHolderApi2.checkOtp(new Register(null,veri_code.getText().toString(),null,null,null,null,null,null),token);
        call.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                if(response.code()==200){
                    //sendToastmsg("Welcome");
                    Intent intent=new Intent(ActivityPhoneVerification.this,MapsActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Request not sent", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }
    private boolean verifyUserOTP() {
        if(veri_code.getText().toString().isEmpty()||veri_code.getText().toString().length()<6){
            veri_code.setError("Invalid OTP");
            veri_code.requestFocus();
            return false;
        }
        else
             return true;
    }
    private boolean verifyPhoneNumber(){
       if(phoneNumber.getText().toString().matches(phonePattern))
           return true;
       else{
           phoneNumber.setError("Please Enter a valid Phone Number");
           phoneNumber.requestFocus();
           return false;
       }
    }
    private void sendfVerificationCode(){
//        contact="+91"+phoneNumber.getText().toString();
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//               contact,
//               60,
//               TimeUnit.SECONDS,
//               this,
//               mCallbacks
//        );
        Retrofit retrofit= RetrofitClientInstance2.getRetrofitInstance();
        JsonPlaceHolderApi2 jsonPlaceHolderApi2=retrofit.create(JsonPlaceHolderApi2.class);
        Call<Register> call=jsonPlaceHolderApi2.getToken(new Register(phoneNumber.getText().toString(),null,null,null,null,null,null,null));
        call.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                if(response.code()==200){
                    Register register=response.body();
                    SharedPreferences sharedPreferences = getSharedPreferences("Token",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("token",register.getToken());
                    editor.apply();
                    progressBar.setVisibility(View.INVISIBLE);
                    veri_code.setVisibility(View.VISIBLE);
                    continue_to_signup.setVisibility(View.VISIBLE);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Request not sent", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            progressDialog.setMessage("Verification Completed...");
            progressDialog.show();
            progressDialog.setCancelable(false);
            mauth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        whetherNewOrOldUser();
                    }
                    else{
                        sendToastmsg(task.getException().getMessage());
                        progressBar.setVisibility(View.INVISIBLE);
                        get_code.setEnabled(true);

                    }
                }

            });

        }
        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            sendToastmsg(e.getMessage());
            progressBar.setVisibility(View.INVISIBLE);
            get_code.setEnabled(true);
        }
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            progressBar.setVisibility(View.INVISIBLE);
            veri_code.setVisibility(View.VISIBLE);
            continue_to_signup.setVisibility(View.VISIBLE);
            verificationId=s;
            token=forceResendingToken;
        }
    };
    private void whetherNewOrOldUser() {
        documentReference=fstore.collection("Users").document(mauth.getCurrentUser().getUid());
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    sendToastmsg("Welcome");
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Name",documentSnapshot.get("Name").toString());
                    editor.putString("Email",documentSnapshot.get("Email Address").toString());
                    editor.putString("Phone",documentSnapshot.get("Phone").toString());
                    if(documentSnapshot.get("Address")!=null)
                    editor.putString("Address",documentSnapshot.get("Address").toString());
                    editor.commit();
                    progressDialog.dismiss();
                    sendToMainActivity();
                }
                else {
                    progressDialog.dismiss();
                    sendToSignUPActivity();
                }
            }
        });
    }
    private void sendToSignUPActivity() {
        Intent intent=new Intent(ActivityPhoneVerification.this,SignUpActivity.class);
        startActivity(intent);
        finish();
    }
    private void sendToMainActivity(){
        CartActivity.updateCartItemModelList();
        Intent intent=new Intent(ActivityPhoneVerification.this,MainActivity.class);
        startActivity(intent);
        finish();

    }
    private void sendToastmsg(String text){
        Toast msg=Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG);
        msg.show();
    }
}
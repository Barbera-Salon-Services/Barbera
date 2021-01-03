package com.barbera.barberaconsumerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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

import java.util.concurrent.TimeUnit;

public class ActivityPhoneVerification extends AppCompatActivity {
    private EditText phoneNumber;
    private CardView get_code;
    private ProgressDialog progressDialog;
    private EditText veri_code;
    private CardView continue_to_signup;
    private FirebaseAuth mauth;
    private String contact;
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

        sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);

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
                    PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationId,veri_code.getText().toString());
                    verifyUser(credential);
                }

            }
        });
    }

    private void verifyUser(PhoneAuthCredential credential) {
        mauth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    sendToastmsg("Verification Completed");
                    whetherNewOrOldUser();
                }
                else{
                    sendToastmsg(task.getException().getMessage());
                    continue_to_signup.setEnabled(true);
                    progressBar.setVisibility(View.INVISIBLE);
                    get_code.setEnabled(true);
                }
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
        contact="+91"+phoneNumber.getText().toString();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
               contact,
               60,
               TimeUnit.SECONDS,
               this,
               mCallbacks
        );
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            progressDialog=new ProgressDialog(ActivityPhoneVerification.this);
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
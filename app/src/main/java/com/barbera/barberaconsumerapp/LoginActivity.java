package com.barbera.barberaconsumerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private CardView login;
    private String emailPattern="[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       // final TextView new_user=(TextView)findViewById(R.id.new_user);
       // final TextView login_using_phone=(TextView) findViewById(R.id.loginUsingPhone);
        email=(EditText) findViewById(R.id.email);
        password=(EditText) findViewById(R.id.Password);
        login=(CardView) findViewById(R.id.Login);
        firebaseAuth=FirebaseAuth.getInstance();
       // final TextView withoutLogin=(TextView)findViewById(R.id.without_login);

       /* withoutLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });*/

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkEmailAndPassword()){
                   final ProgressDialog progressDialog=new ProgressDialog(LoginActivity.this);
                    progressDialog.setMessage("Logging You In...");
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    login.setEnabled(false);
                   /* new_user.setEnabled(false);
                    login_using_phone.setEnabled(false);
                    withoutLogin.setEnabled(false);*/
                    firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        CartActivity.updateCartItemModelList();
                                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                        finish();
                                        progressDialog.dismiss();
                                    }
                                    else{
                                       progressDialog.dismiss();
                                        sendToastmsg(task.getException().getMessage());
                                        login.setEnabled(true);
                                        /*new_user.setEnabled(true);
                                        login_using_phone.setEnabled(true);
                                        withoutLogin.setEnabled(true);*/
                                    }
                                }
                            });


                }
            }
        });

        /*new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    sendToPhoneVerification();
            }
        });*/

      /*  login_using_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               sendToPhoneVerification();
            }
        });*/


    }

    private boolean checkEmailAndPassword() {
        if(email.getText().toString().isEmpty()){
            email.setError("Please Enter a Registered Email Address");
            email.requestFocus();
            return false;
        }
        if(password.getText().toString().isEmpty()||password.getText().toString().length()<8){
            password.setError("Please Enter a Valid Password");
            password.requestFocus();
            return false;
        }
        if(email.getText().toString().matches(emailPattern)){
            return true;
        }
        else{
            email.setError("Enter a Valid Email Address");
            return false;
        }
    }

    private void sendToPhoneVerification(){
        startActivity(new Intent(LoginActivity.this,ActivityPhoneVerification.class));

    }
    private void sendToastmsg(String text){
        Toast msg=Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG);
        msg.show();
    }
}
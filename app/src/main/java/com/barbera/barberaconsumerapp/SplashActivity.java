package com.barbera.barberaconsumerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.barbera.barberaconsumerapp.R.id.dialog_text_progress_dialog;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barberasplashtest);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(dbQueries.slideModelList.size()==0)
         dbQueries.loadslideModelList();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            whetherNewOrOldUser();
        }
        else
            sendToSecondActivity();
    }
    private void sendToSecondActivity() {
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,SecondScreen.class));
                finish();
            }
        },3000);

    }
    private void whetherNewOrOldUser() {

        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    CartActivity.updateCartItemModelList();
                    sendToMainActivity();
                }
                else
                    sendToSignUPActivity();
            }
        });
    }
    private void sendToSignUPActivity() {
        Intent intent=new Intent(SplashActivity.this,SignUpActivity.class);
        startActivity(intent);
        finish();
    }
    private void sendToMainActivity(){
        Intent intent=new Intent(SplashActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
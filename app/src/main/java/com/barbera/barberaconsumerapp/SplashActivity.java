package com.barbera.barberaconsumerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {
    ImageView logo;
    Animation grow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barberasplashtest);
        logo = (ImageView) findViewById(R.id.first_logo);
        grow = AnimationUtils.loadAnimation(this,R.anim.grow);
        logo.setAnimation(grow);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        if(dbQueries.slideModelList.size()==0)
//         dbQueries.loadslideModelList();
//        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
//            whetherNewOrOldUser();
//        }
//        else
            sendToSecondActivity();
    }

    private void sendToSecondActivity() {
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,ActivityPhoneVerification.class));
                finish();
            }
        },3000);
    }

}
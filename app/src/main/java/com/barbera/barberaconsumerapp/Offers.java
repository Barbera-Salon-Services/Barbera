package com.barbera.barberaconsumerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import android.content.ClipboardManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.FirebaseFirestore;

public class Offers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        TextView tt = findViewById(R.id.textView11);
        TextView tt2 = findViewById(R.id.textView13);

        ImageView imageView = findViewById(R.id.link1);
        ImageView imageView2 = findViewById(R.id.link2);

        FirebaseFirestore.getInstance().collection("AppData").document("Earn&Refer").collection("EligibleCustomers")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
                    try{
                        String x= task.getResult().get("used").toString();
                        if(x.equals("Y")) {
                            tt.setText("Already Used");
                            imageView.setVisibility(View.INVISIBLE);
                        }
                        else
                            tt.setText(task.getResult().get("couponCode").toString());
                    }catch (Exception r){
                        tt.setText("Not applied");
                        imageView.setVisibility(View.INVISIBLE);
                    }
                });
        FirebaseFirestore.getInstance().collection("AppData").document("Coupons").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                if(task.getResult().get("CouponName").toString().equals("")) {
                    tt2.setText("No Coupons available");
                    imageView2.setVisibility(View.INVISIBLE);
                }
                else {
                    tt2.setText(task.getResult().get("CouponName").toString());
                    imageView2.setVisibility(View.INVISIBLE);
                }
            }
        });
        imageView.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", tt.getText());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getApplicationContext(),"Copied!",Toast.LENGTH_SHORT).show();
        });
        imageView2.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", tt2.getText());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getApplicationContext(),"Copied!",Toast.LENGTH_SHORT).show();
        });
    }
}
package com.barbera.barberaconsumerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class ReferAndEarn extends AppCompatActivity {
    private Button refer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_and_earn);

        refer=(Button)findViewById(R.id.refer_button);

        refer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FirebaseAuth.getInstance().getCurrentUser()!=null)
                 generateShareCode();
                else {
                    Toast.makeText(getApplicationContext(),"You Must Log In to continue",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),SecondScreen.class));

                }
            }
        });
        FirebaseMessaging.getInstance().subscribeToTopic("refer")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Successful";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }
                    }
                });
    }
    private void generateShareCode() {
        final ProgressDialog progressDialog=new ProgressDialog(ReferAndEarn.this);
        progressDialog.setMessage("Hold on for a moment...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        Uri baseUrl=Uri.parse("http://barbera.netlify.app/custid="+ (FirebaseAuth.getInstance().getCurrentUser().getUid()));
        String domain="https://barbera.page.link";
        String mainLink="https://barbera.page.link/?"+
                "link=http://barbera.netlify.app/?custid="+FirebaseAuth.getInstance().getCurrentUser().getUid()+
                "&apn=com.barbera.barberaconsumerapp"+
                "&st=Barbera"+
                "&sd=Refer and Earn a discount on essential services";
                //"&si=https://firebasestorage.googleapis.com/v0/b/barbera-592f4.appspot.com/o/AppData%2Flogo_final.png?alt=media&token=c8a33d06-fa6c-4fea-a3ed-6d0b68f37212";

        Log.e("profile","Referal Link "+mainLink);

        Task<ShortDynamicLink> shortDynamicLinkTask= FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(Uri.parse(mainLink))
                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if(task.isSuccessful()){
                            Uri shortLink=task.getResult().getShortLink();
                            Log.e("profile","Referal Link "+shortLink.toString());

                            Intent intent=new Intent();
                            String msg=""+"Hey! I'm inviting you to try out Barbera:Salon at home. " +
                                    "They provide salon services to our doorsteps, keeping luxury and safety, " +
                                    "and at very low prices. Download the app from the given link"
                                    +"\n"+shortLink.toString();
                            intent.setAction(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_TEXT,msg);
                           // intent.putExtra(Intent.EXTRA_TEXT,shortLink.toString());
                            intent.setType("text/plain");
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Return to this page after sending invite",Toast.LENGTH_SHORT).show();
                            startActivityForResult(intent,23);
                            refer.setEnabled(true);
                        }
                        else {
                            Toast.makeText(ReferAndEarn.this,task.getException().toString(),Toast.LENGTH_LONG).show();
                            refer.setEnabled(true);
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 23){
            AddUserToReferAndEarn();
        }
    }

    private void AddUserToReferAndEarn() {
        ProgressDialog progressDialog=new ProgressDialog(ReferAndEarn.this);
        progressDialog.setMessage("Hold on for a moment...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        Map<String, Object> map = new HashMap<>();
        String coupon = "BARBERA"+(int)(Math.random()*1000);
        map.put("couponCode",coupon);
        map.put("description","10% off on bookings upto Rs.200");
        map.put("used","N");
        FirebaseFirestore.getInstance().collection("AppData").document("Earn&Refer").collection("EligibleCustomers")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(map).addOnCompleteListener(task -> {
                    Toast.makeText(getApplicationContext(), "Congrats! Eligible for Refer and Earn Coupon!",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                });
    }
}
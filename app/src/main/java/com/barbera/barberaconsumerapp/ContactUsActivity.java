package com.barbera.barberaconsumerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ContactUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_us_new);

        RelativeLayout insta1=(RelativeLayout)findViewById(R.id.insta_in_contact_us_1);
        RelativeLayout insta2=(RelativeLayout)findViewById(R.id.insta_in_contact_us_2);
        RelativeLayout fb=(RelativeLayout)findViewById(R.id.fb_in_contact_us);
        RelativeLayout web=(RelativeLayout)findViewById(R.id.web_in_contact_us);
        RelativeLayout phoneSumit=(RelativeLayout)findViewById(R.id.phone_in_contact_us_1);
        RelativeLayout phoneHimanshu=(RelativeLayout)findViewById(R.id.phone_in_contact_us_2);
        RelativeLayout contactMail=(RelativeLayout)findViewById(R.id.mail_in_contact_us);
        ImageView contactUsLogo=(ImageView)findViewById(R.id.contact_us_logo);


        insta1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri=Uri.parse("https://www.instagram.com/barbera_online_salon/");
                Intent instagram=new Intent(Intent.ACTION_VIEW,uri);
                instagram.setPackage("com.instagram.android");
                try{
                    startActivity(instagram);
                }
                catch (ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.instagram.com/barbera_online_salon/")));
                }
            }
        });
        insta2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri=Uri.parse("https://www.instagram.com/barbera_agra_road/");
                Intent instagram=new Intent(Intent.ACTION_VIEW,uri);
                instagram.setPackage("com.instagram.android");
                try{
                    startActivity(instagram);
                }
                catch (ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.instagram.com/barbera_online_salon/")));
                }
            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri=Uri.parse("https://www.facebook.com/BarberaOnlineSalon");
                Intent instagram=new Intent(Intent.ACTION_VIEW,uri);
                instagram.setPackage("com.facebook.android");
                try{
                    startActivity(instagram);
                }
                catch (ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.facebook.com/BarberaOnlineSalon")));
                }
            }
        });

        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://barbera.netlify.app")));
            }
        });

        phoneSumit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+91 9887048857"));
                startActivity(intent);
            }
        });
        phoneHimanshu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+91 6378791396"));
                startActivity(intent);
            }
        });
        contactMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] emailAddress=new String[]{"barbera.salon@gmail.com"};
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL,emailAddress);
                intent.setType("text/plain");
                startActivity(intent);
            }
        });
        contactUsLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });
    }
}
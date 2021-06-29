package com.barbera.barberaconsumerapp.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.barbera.barberaconsumerapp.R;

public class AboutPrivacyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_privacy);

        TextView heading=(TextView)findViewById(R.id.heading);
        TextView content=(TextView) findViewById(R.id.content);
        ImageView stayHome=(ImageView) findViewById(R.id.stay_home_stay_safe);
        Intent intent=getIntent();
        String HeadingString=intent.getStringExtra("Heading");
        heading.setText(HeadingString);

        if(HeadingString.equals("About Us")){
            content.setText(R.string.aboutUs);
        }
        else {
            content.setText(R.string.privacyPolicy);
            stayHome.setVisibility(View.GONE);
        }
    }
}
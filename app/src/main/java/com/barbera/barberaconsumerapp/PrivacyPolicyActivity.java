package com.barbera.barberaconsumerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PrivacyPolicyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        TextView heading=(TextView)findViewById(R.id.heading);
        TextView content=(TextView) findViewById(R.id.content);
        ImageView stayHome=(ImageView) findViewById(R.id.stay_home_stay_safe);

        heading.setText("User Privacy Policy");
        content.setText(R.string.privacyPolicy);
        stayHome.setVisibility(View.GONE);
    }
}
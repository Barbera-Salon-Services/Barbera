package com.barbera.barberaconsumerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        TextView heading=(TextView)findViewById(R.id.heading);
        TextView content=(TextView) findViewById(R.id.content);

        heading.setText("About Us");
        content.setText(R.string.aboutUs);
    }
}
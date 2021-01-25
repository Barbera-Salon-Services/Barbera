package com.barbera.barberaconsumerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class WeddingPreActivity extends AppCompatActivity {
    private LinearLayout bridal,groom,mehndi,makeup;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wedding_pre);

        bridal=(LinearLayout)findViewById(R.id.bridal_section);
        groom=(LinearLayout)findViewById(R.id.groom_section);
        mehndi=(LinearLayout) findViewById(R.id.mehndi_section);
        makeup=(LinearLayout) findViewById(R.id.makeup_section);


        bridal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getApplicationContext(),WeddingActivity.class);
                intent.putExtra("Type","Bride");
                startActivity(intent);
            }
        });

        groom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getApplicationContext(),WeddingActivity.class);
                intent.putExtra("Type","Groom");
                startActivity(intent);
            }
        });
        mehndi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getApplicationContext(),WeddingActivity2.class);
                intent.putExtra("Type","Mehndi");
                startActivity(intent);
            }
        });
        makeup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(getApplicationContext(),WeddingActivity2.class);
                intent.putExtra("Type","Makeup");
                startActivity(intent);
            }
        });
    }
}
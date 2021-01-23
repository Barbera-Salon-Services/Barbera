package com.barbera.barberaconsumerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class WeddingPreActivity extends AppCompatActivity {
    private LinearLayout bridal;
    private LinearLayout groom;
    private LinearLayout mehandi;
    private LinearLayout makeup;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wedding_pre);

        bridal=(LinearLayout)findViewById(R.id.bridal_section);
        groom=(LinearLayout)findViewById(R.id.groom_section);
        mehandi=(LinearLayout)findViewById(R.id.mehandi_section);
        makeup=(LinearLayout)findViewById(R.id.makeup_section);
        intent=new Intent(getApplicationContext(),WeddingActivity.class);

        bridal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("Type","Bride");
                startActivity(intent);
            }
        });

        groom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("Type","Groom");
                startActivity(intent);
            }
        });
    }
}
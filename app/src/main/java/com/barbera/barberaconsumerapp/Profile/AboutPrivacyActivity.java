package com.barbera.barberaconsumerapp.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.barbera.barberaconsumerapp.Bookings.BookingsActivity;
import com.barbera.barberaconsumerapp.HomeActivity;
import com.barbera.barberaconsumerapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AboutPrivacyActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_privacy);
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id. booking:
                        startActivity(new Intent(getApplicationContext(), BookingsActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id. profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                }
                return false;
            }
        });

/*
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
*/
    }
}
package com.barbera.barberaconsumerapp.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.barbera.barberaconsumerapp.ActivityPhoneVerification;
import com.barbera.barberaconsumerapp.Bookings.BookingsActivity;
import com.barbera.barberaconsumerapp.MainActivity;
import com.barbera.barberaconsumerapp.R;
import com.barbera.barberaconsumerapp.SecondScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {
    private CardView logout;
    private TextView Name;
    private TextView phone;
    private TextView email;
  //  private TextView address;
    private ProgressBar baronprofile;
    public  static String profile_name="";
    public  static String profile_phone="";
    private  static String profile_email="";
    //private static String profile_address="";
    private RelativeLayout shareAndEarn;
    private RelativeLayout profileMainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_new);

        Name=(TextView)findViewById(R.id.EmailInProfile);
        phone=(TextView)findViewById(R.id.PhoneInProfile);
        baronprofile=(ProgressBar)findViewById(R.id.progressBarOnProfile) ;
        RelativeLayout AboutUs=(RelativeLayout) findViewById(R.id.aboutUs_rel_layout);
        RelativeLayout contactus=(RelativeLayout)findViewById(R.id.contact_rel_layout);
        RelativeLayout logout=(RelativeLayout)findViewById(R.id.logout_rel_layout);
        RelativeLayout privacyPOlicy=(RelativeLayout)findViewById(R.id.privacy_rel_layout);
        RelativeLayout myProfile = (RelativeLayout) findViewById(R.id.my_profile_rel_layout);
        shareAndEarn=(RelativeLayout)findViewById(R.id.refer_rel_layout);
        profileMainLayout=(RelativeLayout)findViewById(R.id.profile_section_main_view);
        RelativeLayout couponsLayout=(RelativeLayout)findViewById(R.id.coupons_rel_layout);


        if(profile_name == ""&&FirebaseAuth.getInstance().getCurrentUser()!=null){
            baronprofile.setVisibility(View.VISIBLE);
            FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                profile_name=task.getResult().get("Name").toString();
                                profile_phone=task.getResult().get("Phone").toString();
                                profile_email=task.getResult().get("Email Address").toString();
                                //profile_address=task.getResult().get("Address").toString();
                                Name.setText(profile_name);
                                phone.setText(profile_phone);
//                                email.setText(profile_email);
                                profileMainLayout.setVisibility(View.VISIBLE);
                                baronprofile.setVisibility(View.INVISIBLE);
                            }
                            else
                                Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });

        }
        else{
            Name.setText(profile_name);
            phone.setText(profile_phone);
//            email.setText(profile_email);
            profileMainLayout.setVisibility(View.VISIBLE);
        }
        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,MyProfile.class));
            }
        });

        AboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this, AboutPrivacyActivity.class);
                intent.putExtra("Heading","About Us");
                startActivity(intent);
            }
        });


        contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ContactUsActivity.class));
            }
        });


        privacyPOlicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this, AboutPrivacyActivity.class);
                intent.putExtra("Heading","User Privacy Policy");
                startActivity(intent);
            }
        });

        couponsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MyCoupons.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutfromDevice();

            }
        });
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigation);

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
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                }
                return false;
            }
        });
        shareAndEarn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ReferAndEarn.class));
            }
        });
    }

    private void logoutfromDevice() {
        final AlertDialog.Builder builder=new AlertDialog.Builder(ProfileActivity.this);
        builder.setMessage("Logout From This Device??");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                BookingsActivity.bookingActivityList.clear();
                profile_name="";
                BookingsActivity.checked=false;
                MyCoupons.couponItemModelList.clear();
                MyCoupons.couponsChecked=false;
                //FirebaseAuth.getInstance().signOut();
                SharedPreferences preferences =getSharedPreferences("Token",MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("token","no");
                editor.apply();
                startActivity(new Intent(ProfileActivity.this, ActivityPhoneVerification.class));
                overridePendingTransition(0,0);
                finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog=builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        overridePendingTransition(0,0);
        finish();
        super.onBackPressed();
    }
}
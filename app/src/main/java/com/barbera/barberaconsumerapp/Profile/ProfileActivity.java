package com.barbera.barberaconsumerapp.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.barbera.barberaconsumerapp.ActivityPhoneVerification;
import com.barbera.barberaconsumerapp.Bookings.BookingsActivity;
import com.barbera.barberaconsumerapp.HomeActivity;
import com.barbera.barberaconsumerapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    private static String sharePrefIdentifier = "ProfileImage";
    private TextView Name;
    private TextView phone;
    private TextView email;
  //  private TextView address;
    private ProgressBar baronprofile;
    public  static String profile_name="";
    public  static String profile_phone="";
    private  static String profile_email="";
    //private static String profile_address="";
    private Button shareAndEarn,AboutUs,contactus,logout,privacyPOlicy,couponsLayout,myProfile;
    private RelativeLayout profileMainLayout;
    private ImageView editMyProfile,profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_new);

        Name=(TextView)findViewById(R.id.NameInProfile);
        phone=findViewById(R.id.phone_no);
        AboutUs=findViewById(R.id.aboutUs_rel_layout);
        contactus=findViewById(R.id.contact_rel_layout);
        logout=findViewById(R.id.logout_rel_layout);
        privacyPOlicy=findViewById(R.id.privacy_rel_layout);
        myProfile =  findViewById(R.id.my_profile_rel_layout);
        shareAndEarn=findViewById(R.id.refer_rel_layout);
        couponsLayout=findViewById(R.id.coupons_rel_layout);
        editMyProfile=findViewById(R.id.edit_my_profile);
        profileImage=findViewById(R.id.profile_image);

        SharedPreferences sharedPreferences=getSharedPreferences("Profile",MODE_PRIVATE);
        String nm=sharedPreferences.getString("name","");
        String ph=sharedPreferences.getString("phone","");
        if(!nm.equals("")){
            Name.setText(nm);
        }
        if(!ph.equals("")){
            phone.setText(ph);
        }

        //calling to set profile Image
        setProfileImage();

        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,MyProfile.class));
            }
        });

        editMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this,EditProfile.class));
            }
        });

        AboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfileActivity.this, AboutUsActivity.class);
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
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.profile);
//        bottomNavigationView.getMenu().findItem(R.id.profile).setChecked(true);

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
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
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
                BookingsActivity.checked=false;
                //FirebaseAuth.getInstance().signOut();
                SharedPreferences preferences =getSharedPreferences("Token",MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("token","no");
                editor.apply();
                SharedPreferences sharedPreferences=getSharedPreferences("Profile",MODE_PRIVATE);
                SharedPreferences.Editor editor1=sharedPreferences.edit();
                editor1.putString("name","");
                editor1.putString("phone","");
                editor1.apply();
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
        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        overridePendingTransition(0,0);
        finish();
        super.onBackPressed();
    }

    private void setProfileImage(){
        Log.d("PROFILEACTIVITY","Error setprofileimage");
        if (EditProfile.FLAG==true){
            SharedPreferences sh = getSharedPreferences(sharePrefIdentifier,MODE_PRIVATE);
            String uri = sh.getString("ProfileImageUri","");
            if (uri!=null){
                profileImage.setImageURI(Uri.parse(uri));
            }
            else Toast.makeText(this,"Error Profile Image is not set",Toast.LENGTH_SHORT);
        }
    }
}
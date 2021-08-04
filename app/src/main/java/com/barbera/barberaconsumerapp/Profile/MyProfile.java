package com.barbera.barberaconsumerapp.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.barbera.barberaconsumerapp.R;
import com.barbera.barberaconsumerapp.network_aws.JsonPlaceHolderApi2;
import com.barbera.barberaconsumerapp.network_aws.Register;
import com.barbera.barberaconsumerapp.network_aws.RetrofitClientInstanceUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyProfile extends AppCompatActivity {
    private static String sharePrefIdentifier = "ProfileImage";
    private Button edit;
    private TextView name,email,address,phone;
    private ImageView profileImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        edit=findViewById(R.id.edit_profile);
        name=findViewById(R.id.profile_name);
        email=findViewById(R.id.profile_email);
        address=findViewById(R.id.profile_address);
        phone=findViewById(R.id.profile_phone);
        profileImage=findViewById(R.id.my_profileActivity_profile_image);

        //calling to set profile Image
        setProfileImage();

        SharedPreferences preferences = getSharedPreferences("Token",MODE_PRIVATE);
        String token=preferences.getString("token",null);
        Retrofit retrofit = RetrofitClientInstanceUser.getRetrofitInstance();
        JsonPlaceHolderApi2 jsonPlaceHolderApi2 = retrofit.create(JsonPlaceHolderApi2.class);
        Call<Register> call=jsonPlaceHolderApi2.getProfile("Bearer "+token);
        ProgressDialog progressDialog = new ProgressDialog(MyProfile.this);
        progressDialog.setMessage("Hold on for a moment...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        call.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                if(response.code()==200){
                    Register register=response.body();
                    String nm=register.getName();
                    String ph=register.getPhone();
                    String add=register.getAddress();
                    String em=register.getEmail();

                    if(nm!=null){
                        name.setText(nm);
                    }
                    if(ph!=null){
                        phone.setText(ph);
                    }
                    if(add!=null){
                        address.setText(add);
                    }
                    if(em!=null){
                        email.setText(em);
                    }
                    progressDialog.dismiss();
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Could not load profile",Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

//        Intent intent =getIntent();
//        String name1=intent.getStringExtra("name");
//        String phone1=intent.getStringExtra("phone");
//        String address1=intent.getStringExtra("address");
//        String email1=intent.getStringExtra("email");
//
//        if(name1!=null){
//            name.setText(name1);
//            phone.setText(phone1);
//            address.setText(address1);
//            email.setText(email1);
//        }
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyProfile.this,EditProfile.class);
//                intent.putExtra("name",name.getText().toString());
//                intent.putExtra("email",email.getText().toString());
//                intent.putExtra("address",address.getText().toString());
//                intent.putExtra("phone",phone.getText().toString());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MyProfile.this,ProfileActivity.class));
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
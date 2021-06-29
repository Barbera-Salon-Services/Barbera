package com.barbera.barberaconsumerapp.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.barbera.barberaconsumerapp.R;
import com.barbera.barberaconsumerapp.network_aws.JsonPlaceHolderApi2;
import com.barbera.barberaconsumerapp.network_aws.Register;
import com.barbera.barberaconsumerapp.network_aws.RetrofitClientInstance2;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditProfile extends AppCompatActivity {
    private EditText editName,editPhone,editEmail,editAddress;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        editAddress=findViewById(R.id.edit_profile_address);
        editEmail=findViewById(R.id.edit_profile_email);
        editPhone=findViewById(R.id.edit_profile_phone);
        editName=findViewById(R.id.edit_profile_name);
        save=findViewById(R.id.save);

        SharedPreferences preferences = getSharedPreferences("Token",MODE_PRIVATE);
        String token=preferences.getString("token",null);
        Retrofit retrofit = RetrofitClientInstance2.getRetrofitInstance();
        JsonPlaceHolderApi2 jsonPlaceHolderApi2 = retrofit.create(JsonPlaceHolderApi2.class);
        Call<Register> call=jsonPlaceHolderApi2.getProfile(token);
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
                        editName.setText(nm);
                    }
                    if(ph!=null){
                        editPhone.setText(ph);
                    }
                    if(add!=null){
                        editAddress.setText(add);
                    }
                    if(em!=null){
                        editEmail.setText(em);
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Could not get data", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editAddress.getText().equals("")){
                    Toast.makeText(getApplicationContext(),"Please enter valid address",Toast.LENGTH_SHORT).show();
                }
                else {
                    Retrofit retrofit = RetrofitClientInstance2.getRetrofitInstance();
                    JsonPlaceHolderApi2 jsonPlaceHolderApi2 = retrofit.create(JsonPlaceHolderApi2.class);
                    Call<Register> call1=jsonPlaceHolderApi2.updateProfile(new Register(editPhone.getText().toString(),null,editEmail.getText().toString(),
                            editName.getText().toString(),null,editAddress.getText().toString()),token);
                    call1.enqueue(new Callback<Register>() {
                        @Override
                        public void onResponse(Call<Register> call, Response<Register> response) {
                            if(response.code()!=200){
                                Toast.makeText(getApplicationContext(), "Could not edit profile", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                startActivity(new Intent(EditProfile.this,MyProfile.class));
                            }
                        }

                        @Override
                        public void onFailure(Call<Register> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
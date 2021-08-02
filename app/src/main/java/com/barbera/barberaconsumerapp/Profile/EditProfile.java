package com.barbera.barberaconsumerapp.Profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.barbera.barberaconsumerapp.FilePath;
import com.barbera.barberaconsumerapp.R;
import com.barbera.barberaconsumerapp.network_aws.JsonPlaceHolderApi2;
import com.barbera.barberaconsumerapp.network_aws.Register;
import com.barbera.barberaconsumerapp.network_aws.RetrofitClientInstanceUser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditProfile extends AppCompatActivity {
    private static final String TAG = "EditProfile";
    private EditText editName,editPhone,editEmail,editAddress;
    private Button save,uploadProfileImage;
    private static int PICK_IMAGE_REQUEST = 21;
    private static int STORAGE_PERMISSION_CODE = 211;
    private Uri imageUri;
    private Bitmap bitmap;
    private ImageView profileImage;
    private File file;
    private String encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        editAddress=findViewById(R.id.edit_profile_address);
        editEmail=findViewById(R.id.edit_profile_email);
        editPhone=findViewById(R.id.edit_profile_phone);
        editName=findViewById(R.id.edit_profile_name);
        save=findViewById(R.id.save);
        uploadProfileImage=findViewById(R.id.upload_profile_image);
        profileImage=findViewById(R.id.edit_profile_image);

        SharedPreferences preferences = getSharedPreferences("Token",MODE_PRIVATE);
        String token=preferences.getString("token",null);
        Retrofit retrofit = RetrofitClientInstanceUser.getRetrofitInstance();
        JsonPlaceHolderApi2 jsonPlaceHolderApi2 = retrofit.create(JsonPlaceHolderApi2.class);
        Call<Register> call=jsonPlaceHolderApi2.getProfile("Bearer "+token);
        ProgressDialog progressDialog = new ProgressDialog(EditProfile.this);
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
                    progressDialog.dismiss();
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Could not get data", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        uploadProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editAddress.getText().equals("")){
                    Toast.makeText(getApplicationContext(),"Please enter valid address",Toast.LENGTH_SHORT).show();
                }
                else {
                    Retrofit retrofit = RetrofitClientInstanceUser.getRetrofitInstance();
                    JsonPlaceHolderApi2 jsonPlaceHolderApi2 = retrofit.create(JsonPlaceHolderApi2.class);
                    Call<Register> call1=jsonPlaceHolderApi2.updateProfile(new Register(editPhone.getText().toString(),null,editEmail.getText().toString(),
                            editName.getText().toString(),null,editAddress.getText().toString()),"Bearer "+token);
                    call1.enqueue(new Callback<Register>() {
                        @Override
                        public void onResponse(Call<Register> call, Response<Register> response) {
                            if(response.code()!=200){
                                Toast.makeText(getApplicationContext(), "Could not edit profile", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                SharedPreferences sharedPreferences=getSharedPreferences("Profile",MODE_PRIVATE);
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                editor.putString("address",editAddress.getText().toString());
                                editor.apply();
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

        //asking for the permission to access storage
        requestStoragePermission();
    }

    private void requestStoragePermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            return;
        }
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==211){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission granted",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this,"Permission not granted",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            String path = FilePath.getPath(getApplicationContext(), imageUri);
            file = new File(path);
            profileImage.setImageURI(imageUri);
            Toast.makeText(this, "path" + path + " image Uri " + imageUri, Toast.LENGTH_SHORT).show();

            byte[] bytes = new byte[(int) file.length()];
            try {
                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
                buf.read(bytes, 0, bytes.length);
                buf.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                encodedImage = Base64.getEncoder().encodeToString(bytes);
            }

        }
    }
}
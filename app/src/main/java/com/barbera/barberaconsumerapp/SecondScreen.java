//package com.barbera.barberaconsumerapp;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.cardview.widget.CardView;
//import androidx.core.app.ActivityCompat;
//
//import android.Manifest;
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentSender;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.common.api.ApiException;
//import com.google.android.gms.common.api.ResolvableApiException;
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.location.LocationSettingsRequest;
//import com.google.android.gms.location.LocationSettingsResponse;
//import com.google.android.gms.location.LocationSettingsStatusCodes;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//
//import java.io.IOException;
//
//public class SecondScreen extends AppCompatActivity {
//    private Button login;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_second_screen);
//
//        LocationRequest locationRequest = LocationRequest.create();
//        locationRequest.setInterval(500);
//        locationRequest.setFastestInterval(500);
//        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);
//
//
//
//        // If check required for location enabled, un-comment the following lines code
//
////        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
////        Task<LocationSettingsResponse> task = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());
////        task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
////            @Override
////            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
////                try {
////                    task.getResult(ApiException.class);
////                } catch (ApiException e) {
////                    switch (e.getStatusCode()){
////                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
////                            try {
////                                ResolvableApiException resolvableApiException = (ResolvableApiException)e;
////                                resolvableApiException.startResolutionForResult(SecondScreen.this,800);
////                            } catch (IntentSender.SendIntentException ex) {
////                                ex.printStackTrace();
////                            }
////                            break;
////                        default:
////                            break;
////                    }
////                }
////            }
////        });
//
//        TextView skipLogin=(TextView)findViewById(R.id.skip_login);
//        login=(Button)findViewById(R.id.new_user_signup);
//
//
//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(),ActivityPhoneVerification.class));
//            }
//        });
//    }
//
//}
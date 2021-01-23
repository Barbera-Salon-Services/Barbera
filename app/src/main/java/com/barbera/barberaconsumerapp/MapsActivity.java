package com.barbera.barberaconsumerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.nio.file.FileStore;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    private FusedLocationProviderClient client;
    private LocationRequest locationRequest;
    private LatLng center;
    private LatLng center1;
    private LatLng center2;
    private double radius1;
    private double radius;
    private double radius2;
    private ProgressDialog progressDialog;
    private DocumentReference documentReference;
    private FirebaseFirestore fileStore;
    private FirebaseAuth firebaseAuth;

    private double Lat;
    private double Lon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);

        fileStore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        center =new LatLng(22.640268, 88.390115);
        radius =10000;
        center1 = new LatLng(21.640268, 88.390115);
        radius1=10000;
        center2 =new LatLng(26.474001, 75.341022);
        radius2=10000;

        client = LocationServices.getFusedLocationProviderClient(this);

        if(ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            getCurrentLocation();
        }else {
            ActivityCompat.requestPermissions(MapsActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},4);
        }

    }

    private void getCurrentLocation() {
        progressDialog = new ProgressDialog(MapsActivity.this);
        progressDialog.setMessage("Fetching Your Current Location...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        @SuppressLint("MissingPermission") Task<Location> task =client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    Lat=location.getLatitude();
                    Lon=location.getLongitude();
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(MapsActivity.this);
                }

            }
        });
    }

    @Override
    @SuppressLint("MissingPermission")
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        LatLng mylocation = new LatLng(Lat,Lon);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(mylocation);
        mMap.addMarker(markerOptions);
        mMap.setMyLocationEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 17));

        Circle circle = mMap.addCircle(new CircleOptions()
                .center(center)
                .radius(radius)
                .strokeWidth(5.0f)
                .fillColor(0x1A0066FF)
                .strokeColor(0xFF0066FF));
        Circle circle1 = mMap.addCircle(new CircleOptions()
                .center(center1)
                .radius(radius1)
                .strokeWidth(5.0f)
                .fillColor(0x1A0066FF)
                .strokeColor(0xFF0066FF));
        Circle circle2 = mMap.addCircle(new CircleOptions()
                .center(center2)
                .radius(radius2)
                .strokeWidth(5.0f)
                .fillColor(0x1A0066FF)
                .strokeColor(0xFF0066FF));
        try {
            checkWithinZone(mylocation);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void checkWithinZone(LatLng location) throws IOException {
        double distanceInMeters =getdistanceinkm(location)*1000;
        double distanceInMeters1 = getdistanceinkm1(location)*1000;
        double distanceInMeters2 = getdistanceinkm2(location)*1000;
        documentReference=fileStore.collection("Users").document(firebaseAuth.getCurrentUser().getUid());
        final SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);

        if(distanceInMeters<= radius || distanceInMeters1<=radius1 || distanceInMeters2<= radius2){
            Geocoder geocoder =  new Geocoder(this, Locale.getDefault());
            List<Address> addressList = geocoder.getFromLocation(Lat,Lon, 1);
            final String address = addressList.get(0).getAddressLine(0);
            progressDialog.dismiss();
            final AlertDialog.Builder builder=new AlertDialog.Builder(MapsActivity.this);
            builder.setMessage("Your location address is "+address+". Do you want to continue?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                @SuppressLint("ResourceAsColor")
                public void onClick(final DialogInterface dialog, int which) {
                    SharedPreferences.Editor editor =sharedPreferences.edit();
                    editor.putString("Address",address);
                    editor.commit();

                    Map<String,Object> user=new HashMap<>();
                    user.put("Address",address);

                    documentReference.update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                dialog.dismiss();
                                sendToMainActivity();
                            }
                        }
                    });
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    startActivity(new Intent(MapsActivity.this,MainActivity.class));
                }
            });
            AlertDialog dialog=builder.create();
            dialog.show();
            Toast.makeText(getApplicationContext(),"Within Zone", Toast.LENGTH_SHORT).show();
        }else{
            sharedPreferences.edit().putString("Address","NA");
            sharedPreferences.edit().commit();
           // Toast.makeText(getApplicationContext(),"We are extremely sorry that we currently do " +
                   // "not provide our services in your location. Hope to reach you SOON", Toast.LENGTH_LONG).show();
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Not Active In this Region");
            builder.setMessage("We Currently aren't active in your Region. Hope to Reach you SOON...");
            builder.setCancelable(false);
            builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sendToMainActivity();
                    finish();
                }
            });
            AlertDialog alertDialog=builder.create();
            alertDialog.show();
        }

    }
    private double getdistanceinkm2(LatLng location) {
        double lat1= center2.latitude;
        double lon1= center2. longitude;
        double lat2= location.latitude;
        double lon2 = location.longitude;

        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result
        return(c * r);
    }

    private double getdistanceinkm1(LatLng location) {
        double lat1= center1.latitude;
        double lon1= center1. longitude;
        double lat2= location.latitude;
        double lon2 = location.longitude;

        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result
        return(c * r);
    }

    private void sendToMainActivity() {
        startActivity(new Intent(MapsActivity.this,MainActivity.class));
    }

    private double getdistanceinkm(LatLng location) {
        double lat1= center.latitude;
        double lon1= center. longitude;
        double lat2= location.latitude;
        double lon2 = location.longitude;

        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result
        return(c * r);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==4){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                getCurrentLocation();
        }
    }
}
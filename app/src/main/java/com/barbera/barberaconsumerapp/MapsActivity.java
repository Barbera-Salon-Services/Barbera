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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    private FusedLocationProviderClient client;
    private LocationRequest locationRequest;
    private LatLng center;
    private double radius;
    private ProgressDialog progressDialog;

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

        center =new LatLng(22.640268, 88.390115);
        radius =10000;

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
        .strokeColor(Color.BLUE));

        try {
            checkWithinZone(mylocation);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void checkWithinZone(LatLng location) throws IOException {
        double distanceInMeters =getdistanceinkm(location)*1000;
        if(distanceInMeters<= radius){
            Geocoder geocoder =  new Geocoder(this, Locale.getDefault());
            List<Address> addressList = geocoder.getFromLocation(Lat,Lon, 1);
            final String address = addressList.get(0).getAddressLine(0);
            progressDialog.dismiss();
            final AlertDialog.Builder builder=new AlertDialog.Builder(MapsActivity.this);
            builder.setMessage("Your location address is "+address+". Do you want to continue?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                @SuppressLint("ResourceAsColor")
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
                    sharedPreferences.edit().putString("Address",address);
                    sharedPreferences.edit().commit();
                    startActivity(new Intent(MapsActivity.this,MainActivity.class));
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            AlertDialog dialog=builder.create();
            dialog.show();
            Toast.makeText(getApplicationContext(),"Within Zone", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(),"Outside Zone", Toast.LENGTH_SHORT).show();
        }

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
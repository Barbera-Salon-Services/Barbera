package com.barbera.barberaconsumerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapSearchActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {
    private GoogleMap mMap;
    private SearchView searchView;
    private Marker marker;
    private CardView cardView;
    private Address address;
    private LatLng center;
    private LatLng center1;
    private double radius1;
    private double radius;
    private FloatingActionButton floatingActionButton;
    private FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);

        searchView = findViewById(R.id.location);
        cardView = findViewById(R.id.continueToBooking);
        floatingActionButton = findViewById(R.id.floatingBtn);

        center =new LatLng(22.640268, 88.390115);
        radius =10000;
        center1 = new LatLng(21.640268, 88.390115);
        radius1=10000;

        if(ActivityCompat.checkSelfPermission(MapSearchActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            startSearching();
        }else {
            ActivityCompat.requestPermissions(MapSearchActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},4);
        }
    }

    private void startSearching() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(MapSearchActivity.this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap =googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

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
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapLongClickListener(this);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(center, 10));

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionButton.setEnabled(false);
                LocationRequest locationRequest =locationRequest = LocationRequest.create();
                locationRequest.setInterval(500);
                locationRequest.setFastestInterval(500);
                locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);

                fetchLocation();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint("MissingPermission")
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList =null;
                if(location!=null || location.equals("")){
                    Geocoder geocoder = new Geocoder(MapSearchActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location,1);
                    } catch (Exception e) {
                        cardView.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(),"Cannot Find location. Please re-enter!",Toast.LENGTH_SHORT).show();
                    }
                    if(addressList.size()>0) {
                        address = addressList.get(0);
                        checkWithinZone(new LatLng(address.getLatitude(),address.getLongitude()));
                        if(marker==null){
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(new LatLng(address.getLatitude(),address.getLongitude()));
                            marker =mMap.addMarker(markerOptions);
                        }else {
                            marker.setPosition(new LatLng(address.getLatitude(),address.getLongitude()));
                        }
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(address.getLatitude(),address.getLongitude()), 17));
                        Toast.makeText(getApplicationContext(),address.getAddressLine(0),Toast.LENGTH_SHORT).show();
                    }else{
                        cardView.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(),"Cannot Find location. Please re-enter!",Toast.LENGTH_SHORT).show();
                    }

                    cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addAddress();
                        }
                    });

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void fetchLocation() {
        client =LocationServices.getFusedLocationProviderClient(this);

        @SuppressLint("MissingPermission") Task<Location> task =client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
               if(location!=null){
                   Geocoder geocoder =  new Geocoder(getApplicationContext(), Locale.getDefault());
                   try {
                       List<Address> addressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                       address = addressList.get(0);
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
                   if(marker==null){
                       MarkerOptions markerOptions = new MarkerOptions();
                       markerOptions.position(new LatLng(address.getLatitude(),address.getLongitude()));
                       marker =mMap.addMarker(markerOptions);
                   }else {
                       marker.setPosition(new LatLng(address.getLatitude(),address.getLongitude()));
                   }
                   mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(address.getLatitude(),address.getLongitude()), 17));
                   Toast.makeText(getApplicationContext(),address.getAddressLine(0),Toast.LENGTH_SHORT).show();
                   cardView.setVisibility(View.VISIBLE);

                   cardView.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           addAddress();
                       }
                   });

               }
            }
        });
    }

    private void addAddress() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("New_Address", address.getAddressLine(0));
        editor.commit();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==4){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                startSearching();
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        if(marker !=null){
            marker.remove();
        }
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(latLng.latitude,latLng.longitude));
        markerOptions.draggable(true);
        marker =mMap.addMarker(markerOptions);

        Geocoder geocoder = new Geocoder(MapSearchActivity.this);
        List<Address> addressList=null;
        try {
            addressList = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String address1 = addressList.get(0).getAddressLine(0);
        Toast.makeText(getApplicationContext(),address1,Toast.LENGTH_SHORT).show();
        checkWithinZone(latLng);
        address =addressList.get(0);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAddress();
            }
        });
    }

    private void checkWithinZone(LatLng location) {
        double distanceInMeters =getdistanceinkm(location)*1000;
        double distanceInMeters1 = getdistanceinkm1(location)*1000;
        if(distanceInMeters<= radius || distanceInMeters1<=radius1){
            cardView.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(),"Within Zone",Toast.LENGTH_SHORT).show();
        }else{
            cardView.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(),"Not Within Zone",Toast.LENGTH_SHORT).show();
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
}
package com.barbera.barberaconsumerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapSearchActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private SearchView searchView;
    private Marker marker;
    private CardView cardView;
    private Address address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);

        searchView = findViewById(R.id.location);
        cardView = findViewById(R.id.continueToBooking);

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap =googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint("MissingPermission")
            @Override
            public boolean onQueryTextSubmit(String query) {
                cardView.setVisibility(View.VISIBLE);
                String location = searchView.getQuery().toString();
                List<Address> addressList =null;
                if(location!=null || location.equals("")){
                    Geocoder geocoder = new Geocoder(MapSearchActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location,1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    address = addressList.get(0);
                    mMap.setMyLocationEnabled(true);

                    if(marker==null){
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(address.getLatitude(),address.getLongitude()));
                    marker =mMap.addMarker(markerOptions);
                    }else {
                        marker.setPosition(new LatLng(address.getLatitude(),address.getLongitude()));
                    }

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(address.getLatitude(),address.getLongitude()), 17));
                    cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Geocoder geocoder =  new Geocoder(MapSearchActivity.this, Locale.getDefault());
                            List<Address> addressList1 = null;
                            try {
                                addressList1 = geocoder.getFromLocation(address.getLatitude(),address.getLatitude(), 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("New_Address", addressList1.get(0).getAddressLine(0));
                            editor.commit();
                            finish();
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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==4){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                startSearching();
        }
    }
}
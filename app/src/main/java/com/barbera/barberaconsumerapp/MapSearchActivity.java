package com.barbera.barberaconsumerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapSearchActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnCameraMoveListener ,GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveStartedListener {
    private GoogleMap mMap;
    private SearchView searchView;
    private Marker marker;
    private CardView cardView;
    private Address address;
    private double Lat;
    private double Lon;
    private CameraPosition key;
    public static LatLng center;
    public static LatLng center1;
    public static LatLng center2;
    public static double radius1;
    public static double radius;
    public static double radius2;
    private FloatingActionButton floatingActionButton;
    private FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);

        searchView = findViewById(R.id.location);
        cardView = findViewById(R.id.continueToBooking);
        floatingActionButton = findViewById(R.id.floatingBtn);

      /*  //agra road region
        center =new LatLng(26.930256, 75.875947);
        radius =8101.33;
        //kalwar road region
        center1 = new LatLng(26.949311, 75.714512);
        radius1=1764.76;
        center2 =new LatLng(26.943649, 75.748845);
        radius2=1718.21;*/

        cardView.setOnClickListener(v -> addAddress());

        if(ActivityCompat.checkSelfPermission(MapSearchActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            startSearching();
        }else
            ActivityCompat.requestPermissions(MapSearchActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 4);
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
        key = mMap.getCameraPosition();

//        Circle circle = mMap.addCircle(new CircleOptions()
//                .center(center)
//                .radius(radius)
//                .strokeWidth(5.0f)
//                .fillColor(0x1A0066FF)
//                .strokeColor(0xFF0066FF));
//        Circle circle1 = mMap.addCircle(new CircleOptions()
//                .center(center1)
//                .radius(radius1)
//                .strokeWidth(5.0f)
//                .fillColor(0x1A0066FF)
//                .strokeColor(0xFF0066FF));
//        Circle circle2 = mMap.addCircle(new CircleOptions()
//                .center(center2)
//                .radius(radius2)
//                .strokeWidth(5.0f)
//                .fillColor(0x1A0066FF)
//                .strokeColor(0xFF0066FF));
        mMap.setMyLocationEnabled(true);
        mMap.setOnCameraMoveStartedListener(this);
        mMap.setOnCameraIdleListener (this);
        mMap.setOnCameraMoveListener  (this);
        fetchLocation();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(center, 17));

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { ;
                LocationRequest locationRequest =locationRequest = LocationRequest.create();
                locationRequest.setInterval(500);
                locationRequest.setFastestInterval(500);
                locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);
                if(isLocationEnabled()){
                fetchLocation();
                }else{
                    enableLocation(locationRequest);
                }
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
                        try {
                            checkWithinZone(new LatLng(address.getLatitude(),address.getLongitude()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }else{
                        cardView.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(),"Cannot Find location. Please re-enter!",Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void enableLocation(LocationRequest locationRequest) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        Task<LocationSettingsResponse> task = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());
        task.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    task.getResult(ApiException.class);
                } catch (ApiException e) {
                    switch (e.getStatusCode()){
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException)e;
                                resolvableApiException.startResolutionForResult(MapSearchActivity.this,8080);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager =(LocationManager)getSystemService(LOCATION_SERVICE);
        boolean providerEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(providerEnabled)
            return true;
        return false;
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
                   LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
                   try {
                       checkWithinZone(latLng);
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
            }
        });
    }

    private void addAddress() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("New_Address", address.getAddressLine(0));
        editor.commit();
        BookingPage.houseAddress.setText(address.getAddressLine(0));
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==4){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                startSearching();
        }
    }

    private void checkWithinZone(LatLng location) throws IOException {
        double distanceInMeters =getdistanceinkm(location)*1000;
        double distanceInMeters1 = getdistanceinkm1(location)*1000;
        double distanceInMeters2 = getdistanceinkm2(location)*1000;
        if(distanceInMeters<= radius || distanceInMeters1<=radius1 || distanceInMeters2<=radius2){
            cardView.setVisibility(View.VISIBLE);
            Geocoder geocoder =  new Geocoder(this, Locale.getDefault());
            List<Address> addressList = geocoder.getFromLocation(location.latitude,location.longitude, 1);
            address = addressList.get(0);
            if(marker!= null)
                marker.remove();
            marker= mMap.addMarker(new MarkerOptions().position(new LatLng(location.latitude,location.longitude)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.latitude,location.longitude), 17));
            Toast.makeText(getApplicationContext(),address.getAddressLine(0),Toast.LENGTH_LONG).show();
        }else{
            cardView.setVisibility(View.INVISIBLE);
            if(marker!= null)
                marker.remove();
            marker= mMap.addMarker(new MarkerOptions().position(new LatLng(location.latitude,location.longitude)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.latitude,location.longitude), 17));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 8080){
            switch (resultCode){
                case Activity.RESULT_OK:
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(getApplicationContext(),"Cannot fetch loaction without enabling location services",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
      /*  FirebaseFirestore.getInstance().collection("AppData").document("CoOrdinates").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            GeoPoint geoPoint1=task.getResult().getGeoPoint("kal_1");
                            GeoPoint geoPoint2=task.getResult().getGeoPoint("kal_2");
                            GeoPoint geoPoint=task.getResult().getGeoPoint("ag");
                            radius=task.getResult().getDouble("ag_radius");
                            radius1=task.getResult().getDouble("kal_1_radius");
                            radius2=task.getResult().getDouble("kal_2_radius");
                            center=new LatLng(geoPoint.getLatitude(),geoPoint.getLongitude());
                            center1=new LatLng(geoPoint1.getLatitude(),geoPoint.getLongitude());
                            center2=new LatLng(geoPoint2.getLatitude(),geoPoint.getLongitude());

                        }
                    }
                });*/
    }

    @Override
    public void onCameraMove() {
        Log.d("Call","Onmoved");
    }

    @Override
    public void onCameraIdle() {
        Log.d("Call","Idle");
        if(!key.equals(mMap.getCameraPosition())) {
            if (marker != null)
                marker.remove();
            marker = mMap.addMarker(new MarkerOptions().position(mMap.getCameraPosition().target));
            Lat = mMap.getCameraPosition().target.latitude;
            Lon = mMap.getCameraPosition().target.longitude;
            try {
                checkWithinZone(new LatLng(Lat, Lon));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCameraMoveCanceled() {
        Log.d("Call","Cancelled");
    }

    @Override
    public void onCameraMoveStarted(int i) {
        Log.d("Call","start");
        key = mMap.getCameraPosition();
    }
}
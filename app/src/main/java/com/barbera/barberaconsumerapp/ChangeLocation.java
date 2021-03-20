package com.barbera.barberaconsumerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.barbera.barberaconsumerapp.Utils.PermissionUtils;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.barbera.barberaconsumerapp.SignUpActivity.center10;
import static com.barbera.barberaconsumerapp.SignUpActivity.center11;
import static com.barbera.barberaconsumerapp.SignUpActivity.center12;
import static com.barbera.barberaconsumerapp.SignUpActivity.center3;
import static com.barbera.barberaconsumerapp.SignUpActivity.center4;
import static com.barbera.barberaconsumerapp.SignUpActivity.center5;
import static com.barbera.barberaconsumerapp.SignUpActivity.center6;
import static com.barbera.barberaconsumerapp.SignUpActivity.center7;
import static com.barbera.barberaconsumerapp.SignUpActivity.center8;
import static com.barbera.barberaconsumerapp.SignUpActivity.center9;
import static com.barbera.barberaconsumerapp.SignUpActivity.radius10;
import static com.barbera.barberaconsumerapp.SignUpActivity.radius11;
import static com.barbera.barberaconsumerapp.SignUpActivity.radius12;
import static com.barbera.barberaconsumerapp.SignUpActivity.radius3;
import static com.barbera.barberaconsumerapp.SignUpActivity.radius4;
import static com.barbera.barberaconsumerapp.SignUpActivity.radius5;
import static com.barbera.barberaconsumerapp.SignUpActivity.radius6;
import static com.barbera.barberaconsumerapp.SignUpActivity.radius7;
import static com.barbera.barberaconsumerapp.SignUpActivity.radius8;
import static com.barbera.barberaconsumerapp.SignUpActivity.radius9;

public class ChangeLocation extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnCameraMoveListener ,GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveStartedListener{
    private GoogleMap mMap;
    private FusedLocationProviderClient client;
    private LocationRequest locationRequest;
    private SearchView searchView;
    private TextView addd;
    private CardView cont;
    private String address;
    private Marker marker;
    private CameraPosition key;
    private FloatingActionButton floatingActionButton;
    private ProgressDialog progressDialog;
    private DocumentReference documentReference;
    private FirebaseFirestore fileStore;
    private FirebaseAuth firebaseAuth;
    private SharedPreferences sharedPreferences;
    private Boolean withinzone;

    private LatLng center;
    private LatLng center1;
    private LatLng center2;

    private double radius;
    private double radius1;
    private double radius2;

    private double Lat;
    private double Lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_location);

        addd= findViewById(R.id.addd3);
        cont = findViewById(R.id.continueTo3);
        searchView = findViewById(R.id.location3);
        floatingActionButton = findViewById(R.id.floatbtn3);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);

        center=MapSearchActivity.center;
        center1=MapSearchActivity.center1;
        center2=MapSearchActivity.center2;

        radius =MapSearchActivity.radius;
        radius1=MapSearchActivity.radius1;
        radius2=MapSearchActivity.radius2;
        fileStore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        client = LocationServices.getFusedLocationProviderClient(this);

        floatingActionButton.setOnClickListener(v -> {LocationRequest locationRequest =locationRequest = LocationRequest.create();
            locationRequest.setInterval(500);
            locationRequest.setFastestInterval(500);
            locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);
            if(isLocationEnabled()){
                getCurrentLocation();
            }else{
                enableLocation(locationRequest);
            }
        });
        cont.setOnClickListener(v -> {
            storeTodb();
        });

        if(ActivityCompat.checkSelfPermission(ChangeLocation.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map3);
            mapFragment.getMapAsync(ChangeLocation.this);
        }else {
            if (PermissionUtils.neverAskAgainSelected(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                displayNeverAskAgainDialog();
            } else {
                ActivityCompat.requestPermissions(ChangeLocation.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 4);
            }
        }

    }
    private void displayNeverAskAgainDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("We need to send SMS for performing necessary task. Please permit the permission through "
                + "Settings screen.\n\nSelect Permissions -> Enable permission");
        builder.setCancelable(false);
        builder.setPositiveButton("Permit Manually", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }


    private void getCurrentLocation() {
        progressDialog = new ProgressDialog(ChangeLocation.this);
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
                    progressDialog.dismiss();
                    try {
                        checkWithinZone(new LatLng(Lat,Lon));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                progressDialog.dismiss();
            }
        });
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        key = mMap.getCameraPosition();

//        LatLng mylocation = new LatLng(Lat,Lon);
        mMap.setMyLocationEnabled(true);
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(center, 10));

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
        Circle circle3 = mMap.addCircle(new CircleOptions()
                .center(center3)
                .radius(radius3)
                .strokeWidth(5.0f)
                .fillColor(0x1A0066FF)
                .strokeColor(0xFF0066FF));
        Circle circle4 = mMap.addCircle(new CircleOptions()
                .center(center4)
                .radius(radius4)
                .strokeWidth(5.0f)
                .fillColor(0x1A0066FF)
                .strokeColor(0xFF0066FF));
        Circle circle5 = mMap.addCircle(new CircleOptions()
                .center(center5)
                .radius(radius5)
                .strokeWidth(5.0f)
                .fillColor(0x1A0066FF)
                .strokeColor(0xFF0066FF));
        Circle circle6 = mMap.addCircle(new CircleOptions()
                .center(center6)
                .radius(radius6)
                .strokeWidth(5.0f)
                .fillColor(0x1A0066FF)
                .strokeColor(0xFF0066FF));
        Circle circle7 = mMap.addCircle(new CircleOptions()
                .center(center7)
                .radius(radius7)
                .strokeWidth(5.0f)
                .fillColor(0x1A0066FF)
                .strokeColor(0xFF0066FF));
        Circle circle8 = mMap.addCircle(new CircleOptions()
                .center(center8)
                .radius(radius8)
                .strokeWidth(5.0f)
                .fillColor(0x1A0066FF)
                .strokeColor(0xFF0066FF));
        Circle circle9 = mMap.addCircle(new CircleOptions()
                .center(center9)
                .radius(radius9)
                .strokeWidth(5.0f)
                .fillColor(0x1A0066FF)
                .strokeColor(0xFF0066FF));
        Circle circle10 = mMap.addCircle(new CircleOptions()
                .center(center10)
                .radius(radius10)
                .strokeWidth(5.0f)
                .fillColor(0x1A0066FF)
                .strokeColor(0xFF0066FF));
        Circle circle11 = mMap.addCircle(new CircleOptions()
                .center(center11)
                .radius(radius11)
                .strokeWidth(5.0f)
                .fillColor(0x1A0066FF)
                .strokeColor(0xFF0066FF));
        Circle circle12 = mMap.addCircle(new CircleOptions()
                .center(center12)
                .radius(radius12)
                .strokeWidth(5.0f)
                .fillColor(0x1A0066FF)
                .strokeColor(0xFF0066FF));
//        try {
//            checkWithinZone(mylocation);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(center, 10));
        mMap.setOnCameraMoveStartedListener(this);
        mMap.setOnCameraIdleListener (this);
        mMap.setOnCameraMoveListener  (this);
        getCurrentLocation();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList =null;
                if(location!=null || location.equals("")) {
                    Geocoder geocoder = new Geocoder(ChangeLocation.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Cannot Find location. Please re-enter!", Toast.LENGTH_SHORT).show();
                    }
                    if(addressList.size()>0) {
                        Lat = addressList.get(0).getLatitude();
                        Lon= addressList.get(0).getLongitude();
                        address = addressList.get(0).toString();
                        try {
                            checkWithinZone(new LatLng(Lat, Lon));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
    private boolean isLocationEnabled() {
        LocationManager locationManager =(LocationManager)getSystemService(LOCATION_SERVICE);
        boolean providerEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(providerEnabled)
            return true;
        return false;
    }
    private void checkWithinZone(LatLng location) throws IOException {
        double distanceInMeters = getdistanceinkm(location) * 1000;
        double distanceInMeters1 = getdistanceinkm1(location) * 1000;
        double distanceInMeters2 = getdistanceinkm2(location) * 1000;
        double distanceInMeters3 = getdistanceinkm3(location)*1000;
        double distanceInMeters4 = getdistanceinkm4(location)*1000;
        double distanceInMeters5 = getdistanceinkm5(location)*1000;
        double distanceInMeters6 = getdistanceinkm6(location)*1000;
        double distanceInMeters7 = getdistanceinkm7(location)*1000;
        double distanceInMeters8 = getdistanceinkm8(location)*1000;
        double distanceInMeters9 = getdistanceinkm9(location)*1000;
        double distanceInMeters10 = getdistanceinkm10(location)*1000;
        double distanceInMeters11 = getdistanceinkm11(location)*1000;
        double distanceInMeters12 = getdistanceinkm12(location)*1000;
        documentReference = fileStore.collection("Users").document(firebaseAuth.getCurrentUser().getUid());
        sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);

        if (distanceInMeters <= radius || distanceInMeters1 <= radius1 || distanceInMeters2 <= radius2 || distanceInMeters3<=radius3
                || distanceInMeters4<=radius4 || distanceInMeters5<=radius5 || distanceInMeters6<=radius6 ||
                distanceInMeters7<=radius7 || distanceInMeters8<=radius8 || distanceInMeters9<=radius9 || distanceInMeters10<=radius10
                || distanceInMeters11<=radius11 || distanceInMeters12<=radius12) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addressList = geocoder.getFromLocation(Lat, Lon, 1);
            address = addressList.get(0).getAddressLine(0);
            addd.setText(address);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.latitude, location.longitude), 17));
            if (marker != null)
                marker.remove();
            marker = mMap.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude)));
            Toast.makeText(getApplicationContext(), "Within Zone", Toast.LENGTH_SHORT).show();
            withinzone = true;
        } else {
            sharedPreferences.edit().putString("Address", "NA");
            sharedPreferences.edit().commit();
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.latitude, location.longitude), 14));
            if (marker != null)
                marker.remove();
            marker = mMap.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude)));
            Toast.makeText(getApplicationContext(), "We Currently aren't active in your Region. Hope to Reach you SOON...", Toast.LENGTH_LONG).show();
            withinzone = false;

        }
    }
    private void storeTodb(){
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.putString("Address",address);
        editor.apply();
        if(withinzone) {
            cont.setCardBackgroundColor(Color.BLACK);
            documentReference.get().addOnCompleteListener(task -> {
                Map<String, Object> user = new HashMap<>();
                user.put("Address1", Lat+","+Lon);
                user.put("house_address", address);
                documentReference.update(user).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        sendToBookingPage();
                    }
                });
            });
        }
        else
            cont.setCardBackgroundColor(Color.GRAY);
            Toast.makeText(getApplicationContext(),"Address Not added",Toast.LENGTH_SHORT).show();
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
                                resolvableApiException.startResolutionForResult(ChangeLocation.this,8080);
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

    private void sendToBookingPage() {
        finish();
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
    private double getdistanceinkm3(LatLng location) {
        double lat1= center3.latitude;
        double lon1= center3. longitude;
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
    private double getdistanceinkm4(LatLng location) {
        double lat1= center4.latitude;
        double lon1= center4. longitude;
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
    private double getdistanceinkm5(LatLng location) {
        double lat1= center5.latitude;
        double lon1= center5. longitude;
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
    private double getdistanceinkm6(LatLng location) {
        double lat1= center6.latitude;
        double lon1= center6. longitude;
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
    private double getdistanceinkm7(LatLng location) {
        double lat1= center7.latitude;
        double lon1= center7. longitude;
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
    private double getdistanceinkm8(LatLng location) {
        double lat1= center8.latitude;
        double lon1= center8. longitude;
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
    private double getdistanceinkm9(LatLng location) {
        double lat1= center9.latitude;
        double lon1= center9. longitude;
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
    private double getdistanceinkm10(LatLng location) {
        double lat1= center10.latitude;
        double lon1= center10. longitude;
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
    private double getdistanceinkm11(LatLng location) {
        double lat1= center11.latitude;
        double lon1= center11. longitude;
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
    private double getdistanceinkm12(LatLng location) {
        double lat1= center12.latitude;
        double lon1= center12. longitude;
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
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                finish();
                startActivity(new Intent(this, MapsActivity.class));
            }else {
                PermissionUtils.setShouldShowStatus(this, Manifest.permission.ACCESS_FINE_LOCATION);
            }
        }
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
    public void onCameraMove() {
        Log.d("Call","Onmoved");
    }

    @Override
    public void onCameraMoveStarted(int i) {
        Log.d("Call","start");
        key = mMap.getCameraPosition();
    }
}
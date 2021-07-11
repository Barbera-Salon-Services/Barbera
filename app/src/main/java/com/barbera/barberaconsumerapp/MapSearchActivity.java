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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.barbera.barberaconsumerapp.Bookings.BookingPage;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
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
    public static LatLng center3,center4,center5,center6,center7,center8,center9,center10,center11,center12;
    public static double  radius3,radius4,radius7,radius5,radius6,radius8,radius9,radius10,radius11,radius12;
    private FloatingActionButton floatingActionButton;
    private FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);

        searchView = findViewById(R.id.location);
        cardView = findViewById(R.id.continueToBooking);
        floatingActionButton = findViewById(R.id.floatingBtn);

        cardView.setOnClickListener(v -> addAddress());

        if(ActivityCompat.checkSelfPermission(MapSearchActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            startSearching();
        }else {
            if (PermissionUtils.neverAskAgainSelected(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                displayNeverAskAgainDialog();
            } else {
                ActivityCompat.requestPermissions(MapSearchActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 4);
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

        mMap.setMyLocationEnabled(true);
        mMap.setOnCameraMoveStartedListener(this);
        mMap.setOnCameraIdleListener(this);
        mMap.setOnCameraMoveListener(this);
        fetchLocation();
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(center3, 17));

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
                        Toast.makeText(getApplicationContext(),address.getAddressLine(0),Toast.LENGTH_SHORT).show();
                        try {
                            moveMarker(new LatLng(address.getLatitude(),address.getLongitude()));
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
                       moveMarker(latLng);
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
        editor.apply();
        BookingPage.houseAddress.setText(address.getAddressLine(0));
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==4){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                startSearching();
        }else {
            PermissionUtils.setShouldShowStatus(this, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }
    private void moveMarker(LatLng location) throws IOException {
        cardView.setVisibility(View.VISIBLE);
        Log.d("location",location.latitude+" "+location.longitude);
        if(location.latitude==0.0){
            cardView.setVisibility(View.INVISIBLE);
            if(marker!= null)
                marker.remove();
            marker= mMap.addMarker(new MarkerOptions().position(new LatLng(location.latitude,location.longitude)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.latitude,location.longitude), 17));
            //Toast.makeText(getApplicationContext(),"Not Within Zone",Toast.LENGTH_SHORT).show();
        }
        else{
            Geocoder geocoder =  new Geocoder(this, Locale.getDefault());
            List<Address> addressList=new ArrayList<>();
            try {
                addressList = geocoder.getFromLocation(location.latitude,location.longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            address=addressList.get(0);
            if(marker!= null)
                marker.remove();
            marker= mMap.addMarker(new MarkerOptions().position(new LatLng(location.latitude,location.longitude)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.latitude,location.longitude), 17));
        }

    }

//    private void checkWithinZone(LatLng location) throws IOException {
//        double distanceInMeters3 = getdistanceinkm3(location)*1000;
//        double distanceInMeters4 = getdistanceinkm4(location)*1000;
//        double distanceInMeters5 = getdistanceinkm5(location)*1000;
//        double distanceInMeters6 = getdistanceinkm6(location)*1000;
//        double distanceInMeters7 = getdistanceinkm7(location)*1000;
//        double distanceInMeters8 = getdistanceinkm8(location)*1000;
//        double distanceInMeters9 = getdistanceinkm9(location)*1000;
//        double distanceInMeters10 = getdistanceinkm10(location)*1000;
//        double distanceInMeters11 = getdistanceinkm11(location)*1000;
//        double distanceInMeters12 = getdistanceinkm12(location)*1000;
//        if(distanceInMeters3<=radius3 || distanceInMeters4<=radius4 || distanceInMeters5<=radius5 || distanceInMeters6<=radius6 ||
//                distanceInMeters7<=radius7 || distanceInMeters8<=radius8 || distanceInMeters9<=radius9 || distanceInMeters10<=radius10
//                || distanceInMeters11<=radius11 || distanceInMeters12<=radius12){
//            cardView.setVisibility(View.VISIBLE);
//            Geocoder geocoder =  new Geocoder(this, Locale.getDefault());
//            List<Address> addressList = geocoder.getFromLocation(location.latitude,location.longitude, 1);
//            address = addressList.get(0);
//            if(marker!= null)
//                marker.remove();
//            marker= mMap.addMarker(new MarkerOptions().position(new LatLng(location.latitude,location.longitude)));
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.latitude,location.longitude), 17));
//            Toast.makeText(getApplicationContext(),address.getAddressLine(0),Toast.LENGTH_LONG).show();
//        }else{
//            cardView.setVisibility(View.INVISIBLE);
//            if(marker!= null)
//                marker.remove();
//            marker= mMap.addMarker(new MarkerOptions().position(new LatLng(location.latitude,location.longitude)));
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.latitude,location.longitude), 17));
//            Toast.makeText(getApplicationContext(),"Not Within Zone",Toast.LENGTH_SHORT).show();
//        }
//
//    }
//
//
//    private double getdistanceinkm3(LatLng location) {
//        double lat1= center3.latitude;
//        double lon1= center3. longitude;
//        double lat2= location.latitude;
//        double lon2 = location.longitude;
//
//        lon1 = Math.toRadians(lon1);
//        lon2 = Math.toRadians(lon2);
//        lat1 = Math.toRadians(lat1);
//        lat2 = Math.toRadians(lat2);
//
//        // Haversine formula
//        double dlon = lon2 - lon1;
//        double dlat = lat2 - lat1;
//        double a = Math.pow(Math.sin(dlat / 2), 2)
//                + Math.cos(lat1) * Math.cos(lat2)
//                * Math.pow(Math.sin(dlon / 2),2);
//
//        double c = 2 * Math.asin(Math.sqrt(a));
//
//        // Radius of earth in kilometers. Use 3956
//        // for miles
//        double r = 6371;
//
//        // calculate the result
//        return(c * r);
//    }


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
            Log.d("locationn",Lat+" "+Lon);

            try {
                moveMarker(new LatLng(Lat, Lon));
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
package com.barbera.barberaconsumerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;

import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private EditText name;
    private EditText email;
    private EditText address;
    private EditText password;
    private CardView signup;
    public static LatLng center,center1,center2,center3,center4,center5,center6,center7,center8,center9,center10,center11,center12;
    public static double radius, radius1,radius2, radius3,radius4,radius7,radius5,radius6,radius8,radius9,radius10,radius11,radius12;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore fstore;
    private DocumentReference documentReference;
    private static int pressed=0;
    private long haircutMinPrice;
    private long shavingMinPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name=(EditText)findViewById(R.id.name);
        email=(EditText) findViewById(R.id.email);
        address = (EditText)findViewById(R.id.house_address);
       // address=(EditText) findViewById(R.id.PostalAddress);
        password=(EditText) findViewById(R.id.Password);
        signup=(CardView) findViewById(R.id.signUp);
        firebaseAuth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);
       // privacyPOLICY=(TextView)findViewById(R.id.privacyPolicyOnsignup);
        final ImageView showpassword=(ImageView)findViewById(R.id.showPassword);

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
                                resolvableApiException.startResolutionForResult(SignUpActivity.this,800);
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

        showpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pressed%2==0) {
                    pressed++;
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showpassword.setImageResource(R.drawable.show_password_blue);
                }
                else{
                    pressed++;
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showpassword.setImageResource(R.drawable.show_password);
                }
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(islocationEnabled()) {
                    if (checkInputs() && checkEmailAndPassword()) {
                        signup.setEnabled(false);
                        saveUserData();
                    }
                }else {
                    finish();
                    startActivity(getIntent());
                }
            }
        });

       TextView already=(TextView)findViewById(R.id.Already_have_account);
        already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this,SecondScreen.class));
            }
        });
    }

    private boolean islocationEnabled() {
        LocationManager locationManager =(LocationManager)getSystemService(LOCATION_SERVICE);
        boolean providerEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(providerEnabled)
            return true;
        return false;
    }

    private void saveUserData() {

        final ProgressDialog progressDialog=new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        String userID = firebaseAuth.getCurrentUser().getUid();
        documentReference=fstore.collection("Users").document(userID);
        AuthCredential credential= EmailAuthProvider.getCredential(email.getText().toString(),password.getText().toString());

        firebaseAuth.getCurrentUser().linkWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                }
                else{
                    sendToastmsg(task.getException().getMessage());
                }
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Name",name.getText().toString());
        editor.putString("Email",email.getText().toString());
        editor.putString("Phone",firebaseAuth.getCurrentUser().getPhoneNumber());
        editor.apply();
        Map<String,Object> user=new HashMap<>();
        //user.put("Address",address.getText().toString());
        user.put("Email Address",email.getText().toString());
        user.put("Name",name.getText().toString());
        user.put("Phone",firebaseAuth.getCurrentUser().getPhoneNumber());
        user.put("house_address",address.getText().toString());

        documentReference.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Map<String, Object> myCart=new HashMap<>();
                    myCart.put("cart_list_size",(long) 0);
                    documentReference.collection("UserData").document("MyCart")
                            .set(myCart).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Map<String, Object> myCoupons=new HashMap<>();
                                myCoupons.put("coupons",(long) 0);
                                documentReference.collection("UserData").document("MyCoupons")
                                        .set(myCoupons).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            checkForReferal();
                                            startActivity(new Intent(SignUpActivity.this,MapsActivity.class));
                                            finish();
                                            progressDialog.dismiss();
                                        }
                                    }
                                });
                            }
                            else{
                                sendToastmsg(task.getException().getMessage());
                                signup.setEnabled(true);
                                progressDialog.dismiss();
                            }
                        }
                    });
                }
                else {
                    sendToastmsg(task.getException().getMessage());
                    signup.setEnabled(true);
                    progressDialog.dismiss();
                }
            }
        });

    }

    private void checkForReferal() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        Uri deepLink=null;
                        if(pendingDynamicLinkData!=null){
                            deepLink=pendingDynamicLinkData.getLink();
                            Log.e("main",deepLink.toString());
                            //http://barbera.netlify.app/?custid=1d5ud2XDb5QBbwGyPFVUvQBSXEe2
                            sendRewardToRefree(deepLink);
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        sendToastmsg(e.toString());
                    }
                });
    }

    private void sendRewardToRefree(Uri deepLink) {
        String referLink=deepLink.toString();
        referLink=referLink.substring(referLink.lastIndexOf("=")+1);
        final DocumentReference documentReference= fstore.collection("Users").document(referLink).collection("UserData").document("MyCoupons");
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String hairCutImage="https://firebasestorage.googleapis.com/v0/b/barbera-592f4.appspot.com/o/MenSalonServices%2FWhatsApp%20Image%202020-10-14%20at%209.59.05%20PM%20(1).jpeg?alt=media&token=38fad5e7-46c3-46d7-9931-ea92c3299762";
                    String shaveImage="https://firebasestorage.googleapis.com/v0/b/barbera-592f4.appspot.com/o/MenSalonServices%2FWhatsApp%20Image%202020-10-14%20at%209.59.09%20PM.jpeg?alt=media&token=5f26acff-a662-43b3-882d-29b976e4d505";
                long numberOfCoupons=(long)documentSnapshot.get("coupons")+1;
                String serviceName=numberOfCoupons%2==0?"Foam Shaving":"Haircut";
                long price=numberOfCoupons%2==0?(long)(Math.random()*8+shavingMinPrice):(long)(Math.random()*8+haircutMinPrice);
                String serviceIcon=numberOfCoupons%2==0?shaveImage:hairCutImage;
                Map<String,Object> reward=new HashMap<>();
                reward.put("coupons",(long)numberOfCoupons);
                reward.put("service_"+numberOfCoupons,serviceName);
                reward.put("service_"+numberOfCoupons+"_type","Men");
                reward.put("service_"+numberOfCoupons+"_price",price);
                reward.put("service_"+numberOfCoupons+"_icon",serviceIcon);
                documentReference.update(reward).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                        }
                        else
                            Log.e("Reward",task.getException().toString());
                    }
                });

            }
                else{

                    String hairCutImage="https://firebasestorage.googleapis.com/v0/b/barbera-592f4.appspot.com/o/MenSalonServices%2FWhatsApp%20Image%202020-10-14%20at%209.59.05%20PM%20(1).jpeg?alt=media&token=38fad5e7-46c3-46d7-9931-ea92c3299762";
                    long price=(long)(Math.random()*8+haircutMinPrice);
                    Map<String,Object> reward=new HashMap<>();
                    reward.put("coupons",(long) 1);
                    reward.put("service_1","HairCut");
                    reward.put("service_1_type","Men");
                    reward.put("service_1_price",price);
                    reward.put("service_1_icon",hairCutImage);
                    documentReference.set(reward).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                            }
                            else
                                Log.e("reward",task.getException().toString());
                        }
                    });
                }
            }
        });
    }

    private boolean checkInputs(){
        if(!TextUtils.isEmpty(name.getText())){
            if(!TextUtils.isEmpty(email.getText())){
                if(!TextUtils.isEmpty(password.getText())){
                              return true;
                }
                else {
                    address.setError("Please enter a valid Password");
                    address.requestFocus();
                    return false;
                }
            }
            else {
                email.setError("Email Section Is Empty");
                email.requestFocus();
                //sendToastmsg("Email Section Is Empty");
                return false;

            }

        }
        else{
            name.setError("Name Section Is Empty");
            name.requestFocus();
            //sendToastmsg("Name Section Is Empty");
            return false;

        }

    }
    private boolean checkEmailAndPassword(){
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
        if(email.getText().toString().matches(emailPattern)){
            if (password.length() >= 8) {
                    return true;
            }
            else{
                password.setError("Password Length Should be Greater Than or Equal to 8");
                return false;
            }
        }
        else{
            email.setError("Email Pattern Is Invalid");
            return false;

        }
    }
    private void sendToastmsg(String text){
        Toast msg=Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG);
        msg.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseFirestore.getInstance().collection("AppData").document("CouponPricing").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    haircutMinPrice=(long)task.getResult().get("haircut_min");
                    shavingMinPrice=(long)task.getResult().get("shaving_min");
                }
            }
        });
        FirebaseFirestore.getInstance().collection("AppData").document("CoOrdinates").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            radius=task.getResult().getDouble("ag_radius");
                            radius1=task.getResult().getDouble("kal_1_radius");
                            radius2=task.getResult().getDouble("kal_2_radius");
                            radius3=task.getResult().getDouble("kal_3_radius");
                            radius4=task.getResult().getDouble("kal_4_radius");
                            radius5=task.getResult().getDouble("kal_5_radius");
                            radius6=task.getResult().getDouble("kal_6_radius");
                            radius7=task.getResult().getDouble("kal_7_radius");
                            radius8=task.getResult().getDouble("kal_8_radius");
                            radius9=task.getResult().getDouble("kal_9_radius");
                            radius10=task.getResult().getDouble("kal_10_radius");
                            radius11=task.getResult().getDouble("kal_11_radius");
                            radius12=task.getResult().getDouble("kal_12_radius");
//                            Toast.makeText(getApplicationContext(),"asasc",Toast.LENGTH_SHORT).show();
                            center=new LatLng(task.getResult().getDouble("c1_lat "), task.getResult().getDouble("c1_lon"));
                            center1=new LatLng(task.getResult().getDouble("c2_lat"), task.getResult().getDouble("c2_lon"));
                            center2=new LatLng(task.getResult().getDouble("c3_lat"), task.getResult().getDouble("c3_lon"));
                            center3=new LatLng(task.getResult().getDouble("c4_lat"), task.getResult().getDouble("c4_lon"));
                            center4=new LatLng(task.getResult().getDouble("c5_lat"), task.getResult().getDouble("c5_lon"));
                            center5=new LatLng(task.getResult().getDouble("c6_lat"), task.getResult().getDouble("c6_lon"));
                            center6=new LatLng(task.getResult().getDouble("c7_lat"), task.getResult().getDouble("c7_lon"));
                            center7=new LatLng(task.getResult().getDouble("c8_lat"), task.getResult().getDouble("c8_lon"));
                            center8=new LatLng(task.getResult().getDouble("c9_lat"), task.getResult().getDouble("c9_lon"));
                            center9=new LatLng(task.getResult().getDouble("c10_lat"), task.getResult().getDouble("c10_lon"));
                            center10=new LatLng(task.getResult().getDouble("c11_lat"), task.getResult().getDouble("c11_lon"));
                            center11=new LatLng(task.getResult().getDouble("c12_lat"), task.getResult().getDouble("c12_lon"));
                            center12=new LatLng(task.getResult().getDouble("c13_lat"), task.getResult().getDouble("c13_lon"));

                        }
                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 800)
        {
            switch(resultCode) {
                case Activity.RESULT_OK:
                    break;
                case Activity.RESULT_CANCELED:
                    finish();
                    startActivity(getIntent());
                    break;

            }

        }
    }
}
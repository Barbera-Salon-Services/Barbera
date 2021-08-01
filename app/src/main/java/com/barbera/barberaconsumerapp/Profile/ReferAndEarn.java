package com.barbera.barberaconsumerapp.Profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.barbera.barberaconsumerapp.ActivityPhoneVerification;
import com.barbera.barberaconsumerapp.R;
import com.barbera.barberaconsumerapp.network_aws.Data;
import com.barbera.barberaconsumerapp.network_aws.JsonPlaceHolderApi2;
import com.barbera.barberaconsumerapp.network_aws.RetrofitClientInstanceCoupon;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReferAndEarn extends AppCompatActivity {
    private Button refer;
    private String token;
    private JsonPlaceHolderApi2 jsonPlaceHolderApi2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_and_earn);

        Retrofit retrofit= RetrofitClientInstanceCoupon.getRetrofitInstance();
        jsonPlaceHolderApi2=retrofit.create(JsonPlaceHolderApi2.class);
        SharedPreferences preferences=getSharedPreferences("Token",MODE_PRIVATE);
        token = preferences.getString("token","no");

        refer=(Button)findViewById(R.id.refer_button);

        refer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!token.equals("no"))
                 generateShareCode();
                else {
                    Toast.makeText(getApplicationContext(),"You Must Log In to continue",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), ActivityPhoneVerification.class));
                }
            }
        });
    }
    private void generateShareCode() {
        final ProgressDialog progressDialog=new ProgressDialog(ReferAndEarn.this);
        progressDialog.setMessage("Hold on for a moment...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        Call<Data> call=jsonPlaceHolderApi2.getReferral("Bearer "+token);
        final String[] x = new String[1];
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                Data data=response.body();
                x[0]=data.getRef();
                String mainLink="https://play.google.com/store/apps/details?id=com.barbera.barberaconsumerapp";

                Log.e("profile","Referal Link "+mainLink);

                Intent intent=new Intent();
                String msg=""+"Hey! I'm inviting you to try out Barbera:Salon at home. " +
                        "They provide salon services to our doorsteps, keeping luxury and safety, " +
                        "and at very low prices. Download the app from the given link "+
                        mainLink+" and enter this referral code: "+ x[0];
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,msg);
                // intent.putExtra(Intent.EXTRA_TEXT,shortLink.toString());
                intent.setType("text/plain");
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Return to this page after sending invite",Toast.LENGTH_SHORT).show();
                startActivityForResult(intent,23);
                refer.setEnabled(true);
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 23){
            //AddUserToReferAndEarn();
        }
    }

    private void AddUserToReferAndEarn() {
        ProgressDialog progressDialog=new ProgressDialog(ReferAndEarn.this);
        progressDialog.setMessage("Hold on for a moment...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        Map<String, Object> map = new HashMap<>();
        String coupon = "BARBERA"+(int)(Math.random()*1000);
        map.put("couponCode",coupon);
        map.put("description","10% off on bookings upto Rs.200");
        map.put("used","N");
        FirebaseFirestore.getInstance().collection("AppData").document("Earn&Refer").collection("EligibleCustomers")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(map).addOnCompleteListener(task -> {
                    Toast.makeText(getApplicationContext(), "Congrats! Eligible for Refer and Earn Coupon!",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                });
    }
}
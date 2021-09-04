package com.barbera.barberaconsumerapp.Profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.barbera.barberaconsumerapp.R;
import com.barbera.barberaconsumerapp.Utils.CouponList;
import com.barbera.barberaconsumerapp.Utils.GetCouponItem;
import com.barbera.barberaconsumerapp.network_aws.JsonPlaceHolderApi2;
import com.barbera.barberaconsumerapp.network_aws.RetrofitClientInstanceCoupon;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyCoupons extends AppCompatActivity {
    public List<GetCouponItem> couponItemModelList=new ArrayList<>();
    public static MyCouponAdapter myCouponAdapter;
    private ProgressBar couponbar;
    private RelativeLayout emptyCouponLayout;
    private RecyclerView couponRecyclerView;
    private String token;
    private TextView ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_coupons);

        ref=findViewById(R.id.referral_coupon);
        couponRecyclerView=(RecyclerView)findViewById(R.id.coupons_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        couponRecyclerView.setLayoutManager(layoutManager);
        myCouponAdapter=new MyCouponAdapter(couponItemModelList,this);

        couponbar=(ProgressBar)findViewById(R.id.progressBarOnMyCoupons);
        emptyCouponLayout=(RelativeLayout)findViewById(R.id.empty_coupon_layout);
        Button addCoupon=(Button)findViewById(R.id.add_a_coupon);

        Retrofit retrofit= RetrofitClientInstanceCoupon.getRetrofitInstance();
        JsonPlaceHolderApi2 jsonPlaceHolderApi2=retrofit.create(JsonPlaceHolderApi2.class);
        SharedPreferences preferences = getSharedPreferences("Token", MODE_PRIVATE);
        token = preferences.getString("token", "no");


        addCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 startActivity(new Intent(getApplicationContext(), ReferAndEarn.class));
            }
        });
        Call<CouponList> call=jsonPlaceHolderApi2.getCoupon("Bearer "+token);
        call.enqueue(new Callback<CouponList>() {
            @Override
            public void onResponse(Call<CouponList> call, Response<CouponList> response) {
                if(response.code()==200){
                    CouponList couponList=response.body();
                    List<GetCouponItem> list=couponList.getList();
                    int refam=couponList.getRefQuantity();
                    if(list.size()==0 && refam==0){
                        emptyCouponLayout.setVisibility(View.VISIBLE);
                        couponRecyclerView.setVisibility(View.INVISIBLE);
                    }
                    else{
                        emptyCouponLayout.setVisibility(View.INVISIBLE);
                        couponRecyclerView.setVisibility(View.VISIBLE);
                        ref.setText("Referral code: "+"BARERAREF"+"  Quantity: "+refam);
                        for(GetCouponItem item:list){
                            couponItemModelList.add(item);
                        }
                        couponRecyclerView.setAdapter(myCouponAdapter);
                    }
                    couponbar.setVisibility(View.GONE);
                }
                else{
                    couponbar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Could not get coupons", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CouponList> call, Throwable t) {
                couponbar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
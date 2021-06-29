package com.barbera.barberaconsumerapp.Profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.barbera.barberaconsumerapp.R;
import com.barbera.barberaconsumerapp.ReferAndEarn;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MyCoupons extends AppCompatActivity {
    public static List<CouponItemModel> couponItemModelList=new ArrayList<>();
    public static MyCouponAdapter myCouponAdapter;
    private ProgressBar couponbar;
    private RelativeLayout emptyCouponLayout;
    private RecyclerView couponRecyclerView;
    public static boolean couponsChecked=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_coupons);

        couponRecyclerView=(RecyclerView)findViewById(R.id.coupons_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        couponRecyclerView.setLayoutManager(layoutManager);
        myCouponAdapter=new MyCouponAdapter(couponItemModelList);
        couponRecyclerView.setAdapter(myCouponAdapter);
        couponbar=(ProgressBar)findViewById(R.id.progressBarOnMyCoupons);
        emptyCouponLayout=(RelativeLayout)findViewById(R.id.empty_coupon_layout);
        Button addCoupon=(Button)findViewById(R.id.add_a_coupon);

        if(couponsChecked&&couponItemModelList.size()==0){
            emptyCouponLayout.setVisibility(View.VISIBLE);
            couponRecyclerView.setVisibility(View.INVISIBLE);
        }
        else {
            emptyCouponLayout.setVisibility(View.INVISIBLE);
            couponRecyclerView.setVisibility(View.VISIBLE);

        }

        addCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 startActivity(new Intent(getApplicationContext(), ReferAndEarn.class));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!couponsChecked)
            loadcouponItemModelList();
    }

    private void loadcouponItemModelList() {
        couponbar.setVisibility(View.VISIBLE);
        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getUid()).
                collection("UserData").document("MyCoupons").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            for(long i=1;i<=(long)documentSnapshot.get("coupons");i++){
                                couponItemModelList.add(new CouponItemModel(documentSnapshot.get("service_"+i+"_icon").toString(),
                                        documentSnapshot.get("service_"+i).toString(),documentSnapshot.get("service_"+i+"_price").toString(),
                                        documentSnapshot.get("service_"+i+"_type").toString(), (int) i));
                                myCouponAdapter.notifyDataSetChanged();
                                couponbar.setVisibility(View.INVISIBLE);
                                couponsChecked=true;
                            }
                            couponbar.setVisibility(View.INVISIBLE);
                            couponsChecked=true;
                            if(couponItemModelList.size()==0){
                                couponbar.setVisibility(View.INVISIBLE);
                                emptyCouponLayout.setVisibility(View.VISIBLE);
                                couponRecyclerView.setVisibility(View.INVISIBLE);
                            }

                        }
                        else {
                            couponsChecked=true;
                            couponbar.setVisibility(View.INVISIBLE);
                            emptyCouponLayout.setVisibility(View.VISIBLE);
                            couponRecyclerView.setVisibility(View.INVISIBLE);
                        }
                    }
                });

    }
}
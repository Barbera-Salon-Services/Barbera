package com.barbera.barberaconsumerapp.Services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.barbera.barberaconsumerapp.ActivityPhoneVerification;
import com.barbera.barberaconsumerapp.HomeActivity;
import com.barbera.barberaconsumerapp.Profile.ReferAndEarn;
import com.barbera.barberaconsumerapp.R;
import com.barbera.barberaconsumerapp.Utils.CheckedModel;
import com.barbera.barberaconsumerapp.Utils.ServiceItem;
import com.barbera.barberaconsumerapp.Utils.ServiceList;
import com.barbera.barberaconsumerapp.network_aws.JsonPlaceHolderApi2;
import com.barbera.barberaconsumerapp.network_aws.RetrofitClientInstanceService;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ServiceType extends AppCompatActivity {
    private String token;
    private String Category;
    private String CategoryIMage="";
    private ImageView image;
    public static String salontype;
    private List<ServiceItem> slist=new ArrayList<>();
    private List<ServiceOuterItem> serviceList=new ArrayList<>();
    private ServiceTypeAdapter serviceTypeAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager llm;
    private ImageView img,refer,cart;
    private TextView cartNo;
    private LinearLayout progress_serv;
    private CardView image_serv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_type);
        img=findViewById(R.id.subtype_image);
        recyclerView=findViewById(R.id.service_recycler_view);
        refer=findViewById(R.id.refer);
        cart=findViewById(R.id.cart);
        cartNo=findViewById(R.id.numberOfCartMain);
        progress_serv=findViewById(R.id.progress_serv);
        image_serv=findViewById(R.id.image_serv);
        llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        Retrofit retrofit = RetrofitClientInstanceService.getRetrofitInstance();
        JsonPlaceHolderApi2 jsonPlaceHolderApi2 = retrofit.create(JsonPlaceHolderApi2.class);
        SharedPreferences preferences = getSharedPreferences("Token", MODE_PRIVATE);
        token = preferences.getString("token", "no");

        Intent intent = getIntent();
        salontype = intent.getStringExtra("SalonType");
        Category = intent.getStringExtra("Category");
        CategoryIMage=intent.getStringExtra("ImageUrl");
        Glide.with(getApplicationContext()).load(CategoryIMage).into(img);

        progress_serv.setVisibility(View.VISIBLE);
        image_serv.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        slist=new ArrayList<>();
        serviceList=new ArrayList<>();
        Call<ServiceList> call=jsonPlaceHolderApi2.getAllServices(salontype,new ServiceItem(null, 0, 0, null, 0, null, Category,
                false, null, false, null,null));
        call.enqueue(new Callback<ServiceList>() {
            @Override
            public void onResponse(Call<ServiceList> call, Response<ServiceList> response) {
                if(response.code()==200){
                    ServiceList list=response.body();
                    List<ServiceItem> serviceItemList=list.getServices();
                    int i=0;
                    String sub="";
                    if(serviceItemList.size()!=0){
                        for(ServiceItem serviceItem:serviceItemList){
                            if(i==0){
                                sub=serviceItem.getSubtype();
                                slist.add(serviceItem);
                                i++;
                            }
                            else{
                                if(serviceItem.getSubtype().equals(sub)){
                                    slist.add(serviceItem);
                                }
                                else{
                                    serviceList.add(new ServiceOuterItem(sub,slist));
                                    slist=new ArrayList<>();
//                                for(ServiceOuterItem item:serviceList){
//                                    for(ServiceItem s:item.getServiceItemList()){
//                                        Log.d("kjhg",s.getName());
//                                    }
//                                }
                                    slist.add(serviceItem);
                                    sub=serviceItem.getSubtype();
                                }
                            }
                        }
                        serviceList.add(new ServiceOuterItem(sub,slist));
//                    for(ServiceOuterItem item:serviceList){
//                        Log.d("sdsd",item.getSubtype());
//                        for(ServiceItem serviceItem:item.getServiceItemList()){
//                            Log.d("kjhg",serviceItem.getName());
//                        }
//
//                    }
                        serviceTypeAdapter=new ServiceTypeAdapter(ServiceType.this,serviceList,salontype);
                        recyclerView.setLayoutManager(llm);
                        recyclerView.setAdapter(serviceTypeAdapter);
                        progress_serv.setVisibility(View.GONE);
                        image_serv.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                    else {
                        progress_serv.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "No services present", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    progress_serv.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Could not load services", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServiceList> call, Throwable t) {
                progress_serv.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        refer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ReferAndEarn.class));
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(token.equals("no")){
                    Toast.makeText(getApplicationContext(),"You Must Log In to continue",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), ActivityPhoneVerification.class));
                }

                else
                    startActivity(new Intent(ServiceType.this, CartActivity.class));
            }
        });
    }
    public void loadNumberOnCart(){
        if(token.equals("no"))
            cartNo.setText("0");
        else {
            SharedPreferences sharedPreferences=getSharedPreferences("Count",MODE_PRIVATE);
            int count=sharedPreferences.getInt("count",0);
            cartNo.setText(""+count);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadNumberOnCart();
    }
}
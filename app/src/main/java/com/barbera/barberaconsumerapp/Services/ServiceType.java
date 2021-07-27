package com.barbera.barberaconsumerapp.Services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_type);
        img=findViewById(R.id.subtype_image);
        recyclerView=findViewById(R.id.service_recycler_view);
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
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.show();
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
                    setAdapter();
                    progressDialog.dismiss();
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Could not load services", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServiceList> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setAdapter(){
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(serviceTypeAdapter);
    }
}
package com.barbera.barberaconsumerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.barbera.barberaconsumerapp.Utils.ServiceItem;
import com.barbera.barberaconsumerapp.Utils.ServiceList;
import com.barbera.barberaconsumerapp.network_aws.JsonPlaceHolderApi2;
import com.barbera.barberaconsumerapp.network_aws.RetrofitClientInstanceService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

//import static com.barbera.barberaconsumerapp.R.drawable.bride_background;

public class WeddingActivity extends AppCompatActivity {
    private ImageView weddingHeading;
    public static String weddingType,salontype,token;
    private RecyclerView weddingRecyclerView;
    private static List<WeddingModel> brideList=new ArrayList<>();
    private static List<WeddingModel> groomList=new ArrayList<>();
    private ProgressBar progressBarwedding;
    private LinearLayoutManager bridelayoutmanager;
    private LinearLayoutManager groomlayoutmanager;
    private WeddingAdapter brideAdapter;
    private WeddingAdapter groomAdapter;
    private JsonPlaceHolderApi2 jsonPlaceHolderApi2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wedding);

        Intent intent=getIntent();
        weddingType=intent.getStringExtra("Category");
        String url=intent.getStringExtra("ImageUrl");
        salontype=intent.getStringExtra("SalonType");
        weddingHeading=(ImageView) findViewById(R.id.heading_view);
        Glide.with(this).load(url).into(weddingHeading);

        SharedPreferences preferences=getSharedPreferences("Token",MODE_PRIVATE);
        token = preferences.getString("token","no");
        Retrofit retrofit = RetrofitClientInstanceService.getRetrofitInstance();
        jsonPlaceHolderApi2=retrofit.create(JsonPlaceHolderApi2.class);

        weddingRecyclerView=(RecyclerView) findViewById(R.id.wedding_recycler_view);
        progressBarwedding=(ProgressBar)findViewById(R.id.progress_bar_on_wedding_section);
        bridelayoutmanager=new LinearLayoutManager(getApplicationContext());
        bridelayoutmanager.setOrientation(RecyclerView.HORIZONTAL);
        groomlayoutmanager=new LinearLayoutManager(getApplicationContext());
        groomlayoutmanager.setOrientation(RecyclerView.HORIZONTAL);
        brideAdapter=new WeddingAdapter(brideList,getApplicationContext());
        groomAdapter=new WeddingAdapter(groomList,getApplicationContext());
        RelativeLayout weddingActLayout=(RelativeLayout)findViewById(R.id.weddingLayout);

        //PagerSnapHelper pagerSnapHelper;
        new PagerSnapHelper().attachToRecyclerView(weddingRecyclerView);

        //weddingHeading.setText(weddingType);

      /*  if(weddingType.equals("Bride"))
            weddingHeading.setBackgroundResource(R.drawable.bridal_heading);
        else
            weddingHeading.setBackgroundResource(R.drawable.groom_heading);*/


        if(weddingType.equals("Bridal Packages")&&brideList.size()==0)
            loadbridalList();
        else if(weddingType.equals("Groom's Packages")&&groomList.size()==0)
            loadgroomList();
        else if(weddingType.equals("Bridal Packages")&&brideList.size()!=0) {
            weddingRecyclerView.setLayoutManager(bridelayoutmanager);
            weddingRecyclerView.setAdapter(brideAdapter);
        }
        else if(weddingType.equals("Groom's Packages")&&groomList.size()!=0) {
            weddingRecyclerView.setLayoutManager(groomlayoutmanager);
            weddingRecyclerView.setAdapter(groomAdapter);
        }

    }

    private void loadbridalList() {
        progressBarwedding.setVisibility(View.VISIBLE);
        Call<ServiceList> call=jsonPlaceHolderApi2.getAllServices(salontype,new ServiceItem(null, 0, 0, null, 0, null, weddingType,
                false, null, false, null,null));
        call.enqueue(new Callback<ServiceList>() {
            @Override
            public void onResponse(Call<ServiceList> call, Response<ServiceList> response) {
                if(response.code()==200){
                    ServiceList serviceList=response.body();
                    List<ServiceItem> list=serviceList.getServices();
                    for(ServiceItem item:list){
                        brideList.add(new WeddingModel(item.getName(),item.getDetail(),item.getPrice()));
                    }
                    weddingRecyclerView.setLayoutManager(bridelayoutmanager);
                    weddingRecyclerView.setAdapter(brideAdapter);
                    progressBarwedding.setVisibility(View.INVISIBLE);
                }
                else{
                    progressBarwedding.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),"Could not get bridal list",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServiceList> call, Throwable t) {
                progressBarwedding.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadgroomList(){
        progressBarwedding.setVisibility(View.VISIBLE);
        Call<ServiceList> call=jsonPlaceHolderApi2.getAllServices(salontype,new ServiceItem(null, 0, 0, null, 0, null, weddingType,
                false, null, false, null,null));
        call.enqueue(new Callback<ServiceList>() {
            @Override
            public void onResponse(Call<ServiceList> call, Response<ServiceList> response) {
                if(response.code()==200){
                    ServiceList serviceList=response.body();
                    List<ServiceItem> list=serviceList.getServices();
                    for(ServiceItem item:list){
                        groomList.add(new WeddingModel(item.getName(),item.getDetail(),item.getPrice()));
                    }
                    weddingRecyclerView.setLayoutManager(bridelayoutmanager);
                    weddingRecyclerView.setAdapter(groomAdapter);
                    progressBarwedding.setVisibility(View.INVISIBLE);
                }
                else{
                    progressBarwedding.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),"Could not get bridal list",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServiceList> call, Throwable t) {
                progressBarwedding.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }
}

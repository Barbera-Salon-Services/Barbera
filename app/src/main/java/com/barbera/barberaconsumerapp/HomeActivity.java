package com.barbera.barberaconsumerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.barbera.barberaconsumerapp.Bookings.BookingsActivity;
import com.barbera.barberaconsumerapp.Profile.ProfileActivity;
import com.barbera.barberaconsumerapp.Utils.TypeList;
import com.barbera.barberaconsumerapp.network_aws.JsonPlaceHolderApi2;
import com.barbera.barberaconsumerapp.network_aws.RetrofitClientInstanceService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import eu.dkaratzas.android.inapp.update.Constants;
import eu.dkaratzas.android.inapp.update.InAppUpdateManager;
import eu.dkaratzas.android.inapp.update.InAppUpdateStatus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeActivity extends AppCompatActivity implements InAppUpdateManager.InAppUpdateHandler{

    private RecyclerView menRecyclerView;
    private RecyclerView womenRecyclerView;
    private InAppUpdateManager inAppUpdateManager;
    private RecyclerView weddingRecyclerView;
    private List<String> imgUrl, imgName;
    private String isRegistered,imagebase;
    private JsonPlaceHolderApi2 jsonPlaceHolderApi2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        imagebase="https://barbera-image.s3-ap-south-1.amazonaws.com/";

        SharedPreferences preferences=getSharedPreferences("Token",MODE_PRIVATE);
        isRegistered = preferences.getString("token","no");
        Retrofit retrofit = RetrofitClientInstanceService.getRetrofitInstance();
        jsonPlaceHolderApi2=retrofit.create(JsonPlaceHolderApi2.class);

        addMenGrid();
        addWomenGrid();
        addWeddingGrid();

//        Cart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(isRegistered.equals("no")){
//                    Toast.makeText(getApplicationContext(),"You Must Log In to continue",Toast.LENGTH_LONG).show();
//                    startActivity(new Intent(getApplicationContext(),SecondScreen.class));
//                }
//
//                else
//                    startActivity(new Intent(HomeActivity.this,CartActivity.class));
//            }
//        });
//        weddingSection.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(),WeddingPreActivity.class));
//            }
//        });

        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id. booking:
                        if(isRegistered.equals("no")){
                            Toast.makeText(getApplicationContext(),"You Must Log In to continue",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(),SecondScreen.class));
                        }
                        else {
                            startActivity(new Intent(getApplicationContext(), BookingsActivity.class));
                            overridePendingTransition(0, 0);
                        }
                        finish();
                        return true;
                    case R.id. profile:
                        if(isRegistered.equals("no")){
                            Toast.makeText(getApplicationContext(),"You Must Log In to continue",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(),SecondScreen.class));
                        }
                        else {
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                            overridePendingTransition(0, 0);
                        }
                        finish();
                        return true;
                    case R.id.home:
                        return true;
                }
                return false;
            }
        });

        //app update code

        inAppUpdateManager=InAppUpdateManager.Builder(this,101)
                .resumeUpdates(true)
                .mode(Constants.UpdateMode.IMMEDIATE)
                .snackBarAction("Update Available for Barbera App")
                .snackBarAction("RESTART")
                .handler(this);

        inAppUpdateManager.checkForAppUpdate();
    }

//    public void loadNumberOnCart(){
//        if(isRegistered.equals("no"))
//            NumberOnCartMain.setText("0");
//        else {
//            SharedPreferences sharedPreferences=getSharedPreferences("Count",MODE_PRIVATE);
//            int count=sharedPreferences.getInt("count",0);
//            NumberOnCartMain.setText(""+count);
//        }
//    }

    private void addWeddingGrid() {

        weddingRecyclerView = findViewById(R.id.wedding_recycler_view);

        imgName = new ArrayList<>();
        imgUrl = new ArrayList<>();
        GridAdapter gridAdapter;

        imgName.add("Bridal Packages\nStarting@ Rs. 5599");
        imgName.add("Groom's Packages\nStarting@ Rs. 2599");

        imgUrl.add("https://i.ibb.co/8rtx241/image.png");
        imgUrl.add("https://i.ibb.co/840KRbC/image.png");

        gridAdapter = new GridAdapter(imgUrl, imgName, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        weddingRecyclerView.setLayoutManager(gridLayoutManager);
        weddingRecyclerView.setAdapter(gridAdapter);
    }

    private void addWomenGrid() {
        womenRecyclerView = findViewById(R.id.women_recycler_view);

        imgName = new ArrayList<>();
        imgUrl = new ArrayList<>();
        GridAdapter gridAdapter;

        ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.show();
        Call<TypeList> call=jsonPlaceHolderApi2.getTypes("female");
        call.enqueue(new Callback<TypeList>() {
            @Override
            public void onResponse(Call<TypeList> call, Response<TypeList> response) {
                if(response.code()==200){
                    TypeList serviceList=response.body();
                    List<String> list=serviceList.getTypeList();
                    if(list.size()!=0){
                        for(String item:list){
                            imgName.add(item);

                        }
                    }

                    //gridView.setAdapter(adapter);
                    progressBar.dismiss();
                }
                else{
                    progressBar.dismiss();
                    Toast.makeText(getApplicationContext(),"Could not get services",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TypeList> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

//        imgName.add("Hair Cut");
//        imgName.add("Hair Care");
//        imgName.add("Hair Color");
//        imgName.add("Facial");
//        imgName.add("Head Massage");
//        imgName.add("Bleech");


        imgUrl.add("https://i.ibb.co/MVmwpZH/image.png");
        imgUrl.add("https://i.ibb.co/6tn2gFk/image.png");
        imgUrl.add("https://i.ibb.co/SXLk7XW/image.png");
        imgUrl.add("https://i.ibb.co/f0MFcHx/image.png");
        imgUrl.add("https://i.ibb.co/j9K4bDL/image.png");
        imgUrl.add("https://i.ibb.co/HDyvXxp/image.png");

        gridAdapter = new GridAdapter(imgUrl, imgName, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        womenRecyclerView.setLayoutManager(gridLayoutManager);
        womenRecyclerView.setAdapter(gridAdapter);

    }

    private void addMenGrid() {
        imgName = new ArrayList<>();
        imgUrl = new ArrayList<>();
        GridAdapter gridAdapter;

        ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.show();
        Call<TypeList> call=jsonPlaceHolderApi2.getTypes("male");
        call.enqueue(new Callback<TypeList>() {
            @Override
            public void onResponse(Call<TypeList> call, Response<TypeList> response) {
                if(response.code()==200){
                    TypeList serviceList=response.body();
                    List<String> list=serviceList.getTypeList();
                    if(list.size()!=0){
                        for(String item:list){
                            imgName.add(item);
                        }
                    }
                    //gridView.setAdapter(adapter);
                    progressBar.dismiss();
                }
                else{
                    progressBar.dismiss();
                    Toast.makeText(getApplicationContext(),"Could not get services",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TypeList> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

//        imgName.add("Hair Cut");
//        imgName.add("Shaving");
//        imgName.add("Hair Color");
//        imgName.add("Facial");
//        imgName.add("Head Massage");
//        imgName.add("Bleech");

        imgUrl.add("https://i.ibb.co/t4z5Vqp/image.png");
        imgUrl.add("https://i.ibb.co/7zYTRbs/image.png");
        imgUrl.add("https://i.ibb.co/mGqJLmJ/image.png");
        imgUrl.add("https://i.ibb.co/PFWzrPb/image.png");
        imgUrl.add("https://i.ibb.co/smdS7FT/image.png");
        imgUrl.add("https://i.ibb.co/Ht4gYjf/image.png");

        gridAdapter = new GridAdapter(imgUrl, imgName, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        menRecyclerView = findViewById(R.id.men_recycler_view);
        menRecyclerView.setLayoutManager(gridLayoutManager);
        menRecyclerView.setAdapter(gridAdapter);

    }
    @Override
    public void onInAppUpdateError(int code, Throwable error) {

    }
    @Override
    public void onInAppUpdateStatus(InAppUpdateStatus status) {
        if(status.isDownloaded()){
            View view=getWindow().getDecorView().findViewById(android.R.id.content);
            Snackbar snackbar=Snackbar.make(view,"Update Available for Barbera App",Snackbar.LENGTH_INDEFINITE);

            snackbar.setAction("", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inAppUpdateManager.completeUpdate();
                }
            });
            snackbar.show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }
}
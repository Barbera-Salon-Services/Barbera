package com.barbera.barberaconsumerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.barbera.barberaconsumerapp.Bookings.BookingsActivity;
import com.barbera.barberaconsumerapp.Profile.ProfileActivity;
import com.barbera.barberaconsumerapp.Profile.ReferAndEarn;
import com.barbera.barberaconsumerapp.Services.GridAdapter;
import com.barbera.barberaconsumerapp.Utils.TypeList;
import com.barbera.barberaconsumerapp.network_aws.JsonPlaceHolderApi2;
import com.barbera.barberaconsumerapp.network_aws.RetrofitClientInstanceService;
import com.denzcoskun.imageslider.adapters.ViewPagerAdapter;
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
    private GridAdapter gridAdapterMen,gridAdapterWomen,gridAdapterWed;
    private RecyclerView womenRecyclerView;
    private InAppUpdateManager inAppUpdateManager;
    private RecyclerView weddingRecyclerView;
    private List<String> imgUrlMen=new ArrayList<>(), imgNameMen=new ArrayList<>(),imgUrlWomen=new ArrayList<>(),imgNameWomen=new ArrayList<>(),imgUrlWed=new ArrayList<>(),imgNameWed=new ArrayList<>();
    private String isRegistered,imagebase;
    private JsonPlaceHolderApi2 jsonPlaceHolderApi2;
    private ImageView Cart;
    private static TextView NumberOnCartMain;
    public static CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        menRecyclerView = findViewById(R.id.men_recycler_view);
        weddingRecyclerView = findViewById(R.id.wedding_recycler_view);
        womenRecyclerView = findViewById(R.id.women_recycler_view);
        NumberOnCartMain=(TextView)findViewById(R.id.numberOfCartMain);
        Cart=(ImageView)findViewById(R.id.cart);
        cartAdapter=new CartAdapter(this);
        ImageView referMain=(ImageView)findViewById(R.id.refer);

        gridAdapterWed = new GridAdapter(imgUrlWed, imgNameWed, this,"wedding");
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        weddingRecyclerView.setLayoutManager(gridLayoutManager1);

        gridAdapterWomen= new GridAdapter(imgUrlWomen, imgNameWomen, HomeActivity.this,"female");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(HomeActivity.this, 3, GridLayoutManager.VERTICAL, false);
        womenRecyclerView.setLayoutManager(gridLayoutManager);

        gridAdapterMen = new GridAdapter(imgUrlMen, imgNameMen, HomeActivity.this,"male");
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(HomeActivity.this, 3, GridLayoutManager.VERTICAL, false);
        menRecyclerView.setLayoutManager(gridLayoutManager2);

        imagebase="https://barbera-image.s3-ap-south-1.amazonaws.com/";


        SharedPreferences preferences=getSharedPreferences("Token",MODE_PRIVATE);
        isRegistered = preferences.getString("token","no");
        Retrofit retrofit = RetrofitClientInstanceService.getRetrofitInstance();
        jsonPlaceHolderApi2=retrofit.create(JsonPlaceHolderApi2.class);


        addMenGrid();
        addWeddingGrid();
        addWomenGrid();

        referMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ReferAndEarn.class));
            }
        });

        Cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRegistered.equals("no")){
                    Toast.makeText(getApplicationContext(),"You Must Log In to continue",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),SecondScreen.class));
                }

                else
                    startActivity(new Intent(HomeActivity.this,CartActivity.class));
            }
        });
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



    public void loadNumberOnCart(){
        if(isRegistered.equals("no"))
            NumberOnCartMain.setText("0");
        else {
            SharedPreferences sharedPreferences=getSharedPreferences("Count",MODE_PRIVATE);
            int count=sharedPreferences.getInt("count",0);
            NumberOnCartMain.setText(""+count);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadNumberOnCart();
    }

    private void addWeddingGrid() {
        imgNameWed.clear();
        imgUrlWed.clear();


        imgNameWed.add("Bridal Packages\nStarting@ Rs. 5599");
        imgNameWed.add("Groom's Packages\nStarting@ Rs. 2599");

        imgUrlWed.add("https://i.ibb.co/8rtx241/image.png");
        imgUrlWed.add("https://i.ibb.co/840KRbC/image.png");


        weddingRecyclerView.setAdapter(gridAdapterWed);
    }

    private void addWomenGrid() {
        imgNameWomen.clear();
        imgUrlWomen.clear();

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
                            imgNameWomen.add(item);
                            item=item.replaceAll(" ","_").toLowerCase();
                            imgUrlWomen.add(imagebase+"female"+item);
                        }
                    }

                    womenRecyclerView.setAdapter(gridAdapterWomen);
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


//        imgUrl.add("https://i.ibb.co/MVmwpZH/image.png");
//        imgUrl.add("https://i.ibb.co/6tn2gFk/image.png");
//        imgUrl.add("https://i.ibb.co/SXLk7XW/image.png");
//        imgUrl.add("https://i.ibb.co/f0MFcHx/image.png");
//        imgUrl.add("https://i.ibb.co/j9K4bDL/image.png");
//        imgUrl.add("https://i.ibb.co/HDyvXxp/image.png");



    }

    private void addMenGrid() {
        imgNameMen.clear();
        imgUrlMen.clear();

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
                            imgNameMen.add(item);
                            item=item.replaceAll(" ","_").toLowerCase();
                            Log.d("item",item);
                            imgUrlMen.add(imagebase+"male"+item);
                        }
                    }

                    menRecyclerView.setAdapter(gridAdapterMen);
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

//        imgUrl.add("https://i.ibb.co/t4z5Vqp/image.png");
//        imgUrl.add("https://i.ibb.co/7zYTRbs/image.png");
//        imgUrl.add("https://i.ibb.co/mGqJLmJ/image.png");
//        imgUrl.add("https://i.ibb.co/PFWzrPb/image.png");
//        imgUrl.add("https://i.ibb.co/smdS7FT/image.png");
//        imgUrl.add("https://i.ibb.co/Ht4gYjf/image.png");



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
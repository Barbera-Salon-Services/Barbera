package com.barbera.barberaconsumerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.barbera.barberaconsumerapp.Services.CartActivity;
import com.barbera.barberaconsumerapp.Services.CartAdapter;
import com.barbera.barberaconsumerapp.Services.GridAdapter;
import com.barbera.barberaconsumerapp.Services.HomeActivityTopImageViewAdapter;
import com.barbera.barberaconsumerapp.Services.ServiceType;
import com.barbera.barberaconsumerapp.Services.SliderAdapter;
import com.barbera.barberaconsumerapp.Services.WeddingPackageAdapter;
import com.barbera.barberaconsumerapp.Utils.SliderItem;
import com.barbera.barberaconsumerapp.Utils.SliderList;
import com.barbera.barberaconsumerapp.Utils.TypeList;
import com.barbera.barberaconsumerapp.network_aws.JsonPlaceHolderApi2;
import com.barbera.barberaconsumerapp.network_aws.RetrofitClientInstanceService;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
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
    private GridAdapter gridAdapterMen,gridAdapterWomen;
    private WeddingPackageAdapter gridAdapterWed;
    private RecyclerView womenRecyclerView;
    private InAppUpdateManager inAppUpdateManager;
    private RecyclerView weddingRecyclerView;
    private List<String> imgUrlMen1=new ArrayList<>(),imgUrlMen=new ArrayList<>(), imgNameMen=new ArrayList<>(),
            imgNameMen1=new ArrayList<>(),imgUrlWomen=new ArrayList<>(),imgUrlWomen1=new ArrayList<>(),
            imgNameWomen=new ArrayList<>(),imgNameWomen1=new ArrayList<>(),imgUrlWed=new ArrayList<>(),imgNameWed=new ArrayList<>();
    private List<SlideModel> sliderItems=new ArrayList<>();
    private List<SliderItem> tabItems=new ArrayList<>();
    private String isRegistered,imagebase;
    private JsonPlaceHolderApi2 jsonPlaceHolderApi2;
    private ImageView Cart;
    private static TextView NumberOnCartMain;
    public static CartAdapter cartAdapter;
    private ImageSlider imageSlider;
    private SliderAdapter sliderAdapter;
    private HomeActivityTopImageViewAdapter tabAdapter;
    private RecyclerView tabRecyclerView;
    private CardView seeMen,seeWomen;
    private TextView seeMoreMen,seeMoreWomen;
    private int a=0,b=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        seeMen=findViewById(R.id.see_more_men);
        seeWomen=findViewById(R.id.see_more_women);
        seeMoreMen=findViewById(R.id.see_men);
        seeMoreWomen=findViewById(R.id.see_women);
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        menRecyclerView = findViewById(R.id.men_recycler_view);
        weddingRecyclerView = findViewById(R.id.wedding_recycler_view);
        womenRecyclerView = findViewById(R.id.women_recycler_view);
        NumberOnCartMain=(TextView)findViewById(R.id.numberOfCartMain);
        Cart=(ImageView)findViewById(R.id.cart);

        cartAdapter=new CartAdapter(this);
        imageSlider=findViewById(R.id.imageSlider);
        ImageView referMain=(ImageView)findViewById(R.id.refer);
        tabRecyclerView=findViewById(R.id.top_recycler_view);

        tabAdapter=new HomeActivityTopImageViewAdapter(this,tabItems);
        LinearLayoutManager tabLlm=new LinearLayoutManager(this);
        tabLlm.setOrientation(RecyclerView.HORIZONTAL);
        tabRecyclerView.setLayoutManager(tabLlm);

//        sliderAdapter=new SliderAdapter(sliderItems,HomeActivity.this);
//        LinearLayoutManager slidLlm=new LinearLayoutManager(this,RecyclerView.HORIZONTAL,true);
//        sliderRecyclerView.setLayoutManager(slidLlm);

        gridAdapterWed = new WeddingPackageAdapter(imgUrlWed, imgNameWed, this,"Wedding_Packages");
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        weddingRecyclerView.setLayoutManager(gridLayoutManager1);

        gridAdapterWomen= new GridAdapter(imgUrlWomen, imgNameWomen, HomeActivity.this,"Womens_Section");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(HomeActivity.this, 3, GridLayoutManager.VERTICAL, false);
        womenRecyclerView.setLayoutManager(gridLayoutManager);

        gridAdapterMen = new GridAdapter(imgUrlMen, imgNameMen, HomeActivity.this,"Mens_Section");
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(HomeActivity.this, 3, GridLayoutManager.VERTICAL, false);
        menRecyclerView.setLayoutManager(gridLayoutManager2);

        imagebase="https://barbera-image.s3.ap-south-1.amazonaws.com/";

        SharedPreferences preferences=getSharedPreferences("Token",MODE_PRIVATE);
        isRegistered = preferences.getString("token","no");
        Retrofit retrofit = RetrofitClientInstanceService.getRetrofitInstance();
        jsonPlaceHolderApi2=retrofit.create(JsonPlaceHolderApi2.class);

        loadTabs();
        loadImageSlider();
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
                    startActivity(new Intent(getApplicationContext(),ActivityPhoneVerification.class));
                }

                else
                    startActivity(new Intent(HomeActivity.this, CartActivity.class));
            }
        });
        seeMen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a==0){
                    for(String s:imgNameMen1){
                        imgNameMen.add(s);
                    }
                    for(String s:imgUrlMen1){
                        imgUrlMen.add(s);
                    }
                    menRecyclerView.setAdapter(gridAdapterMen);
                    seeMoreMen.setText("See less");
                    a=1;
                }
                else {
                    for(String s:imgNameMen1){
                        imgNameMen.remove(s);
                    }
                    for(String s:imgUrlMen1){
                        imgUrlMen.remove(s);
                    }
                    menRecyclerView.setAdapter(gridAdapterMen);
                    seeMoreMen.setText("See all");
                    a=0;
                }
            }
        });
        seeWomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(b==0){
                    for(String s:imgNameWomen1){
                        imgNameWomen.add(s);
                    }
                    for(String s:imgUrlWomen1){
                        imgUrlWomen.add(s);
                    }
                    womenRecyclerView.setAdapter(gridAdapterWomen);
                    seeMoreWomen.setText("See less");
                    b=1;
                }
                else {
                    for(String s:imgNameWomen1){
                        imgNameWomen.remove(s);
                    }
                    for(String s:imgUrlWomen1){
                        imgUrlWomen.remove(s);
                    }
                    womenRecyclerView.setAdapter(gridAdapterWomen);
                    seeMoreWomen.setText("See all");
                    b=0;
                }
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
                            startActivity(new Intent(getApplicationContext(),ActivityPhoneVerification.class));
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
                            startActivity(new Intent(getApplicationContext(),ActivityPhoneVerification.class));
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
    private void loadTabs(){
//        tabItems=new ArrayList<>();
        ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.show();
        Call<SliderList> call=jsonPlaceHolderApi2.getTabs();
        call.enqueue(new Callback<SliderList>() {
            @Override
            public void onResponse(Call<SliderList> call, Response<SliderList> response) {
                if(response.code()==200){
                    SliderList sliderList=response.body();
                    List<SliderItem> list=sliderList.getList();
                    for(SliderItem item:list){
                        tabItems.add(item);
                    }
                    tabRecyclerView.setAdapter(tabAdapter);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Could not load slider",Toast.LENGTH_SHORT).show();
                }
                progressBar.dismiss();
            }

            @Override
            public void onFailure(Call<SliderList> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadImageSlider() {
//        sliderItems=new ArrayList<>();
        ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.show();
        Call<SliderList> call=jsonPlaceHolderApi2.getSlider();
        call.enqueue(new Callback<SliderList>() {
            @Override
            public void onResponse(Call<SliderList> call, Response<SliderList> response) {
                if(response.code()==200){
                    SliderList sliderList=response.body();
                    List<SliderItem> list=sliderList.getList();
                    int i=0;
                    HashMap<Integer,String> cat=new HashMap<>();
                    HashMap<Integer,String> typ=new HashMap<>();
                    HashMap<Integer,String> url=new HashMap<>();
                    HashMap<Integer, Boolean> click=new HashMap<>();
                    for(SliderItem item:list){
                        sliderItems.add(new SlideModel(item.getUrl(),null));
                        cat.put(i,item.getCategory().replaceAll(" ","_"));
                        typ.put(i,item.getTypes().replaceAll(" ","_"));
                        url.put(i,item.getUrl());
                        click.put(i,item.isClickable());
                        //Log.d("dd",item.isClickable()+"");
                        i++;
                    }
                    imageSlider.setImageList(sliderItems, true);
                    imageSlider.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onItemSelected(int i) {
                            if(click.get(i)){
                                Intent intent=new Intent(HomeActivity.this, ServiceType.class);
                                intent.putExtra("SalonType",cat.get(i));
                                intent.putExtra("Category",typ.get(i));
                                intent.putExtra("ImageUrl",url.get(i));
                                startActivity(intent);
                            }
                        }
                    });
//                    sliderRecyclerView.setAdapter(sliderAdapter);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Could not load slider",Toast.LENGTH_SHORT).show();
                }
                progressBar.dismiss();
            }

            @Override
            public void onFailure(Call<SliderList> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

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

        ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.show();
        Call<TypeList> call=jsonPlaceHolderApi2.getTypes("Wedding_Packages");
        call.enqueue(new Callback<TypeList>() {
            @Override
            public void onResponse(Call<TypeList> call, Response<TypeList> response) {
                if(response.code()==200){
                    TypeList serviceList=response.body();
                    List<String> list=serviceList.getTypeList();
                    //int z=0,f=0;
                    if(list.size()!=0){
                        for(String item:list){
                            imgNameWed.add(item);
                            item=item.replaceAll(" ","_");
                            imgUrlWed.add(imagebase+"Wedding_Packages"+item);
                        }
                        weddingRecyclerView.setAdapter(gridAdapterWed);
                        LinearLayoutManager tabLlm=new LinearLayoutManager(getApplicationContext());
                        tabLlm.setOrientation(RecyclerView.HORIZONTAL);
                        weddingRecyclerView.setLayoutManager(tabLlm);
                    }
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


        weddingRecyclerView.setAdapter(gridAdapterWed);
    }

    private void addWomenGrid() {
        imgNameWomen.clear();
        imgUrlWomen.clear();

        ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.show();
        Call<TypeList> call=jsonPlaceHolderApi2.getTypes("Womens_Section");
        call.enqueue(new Callback<TypeList>() {
            @Override
            public void onResponse(Call<TypeList> call, Response<TypeList> response) {
                if(response.code()==200){
                    TypeList serviceList=response.body();
                    List<String> list=serviceList.getTypeList();
                    int z=0,f=0;
                    if(list.size()!=0){
                        for(String item:list){
                            if(z<6){
                                imgNameWomen.add(item);
                                item=item.replaceAll(" ","_");
                                imgUrlWomen.add(imagebase+"Womens_Section"+item);
                                z++;
                            }
                            else{
                                f=1;
                                imgNameWomen1.add(item);
                                item=item.replaceAll(" ","_");
                                imgUrlWomen1.add(imagebase+"Womens_Section"+item);
                            }
                        }
                        if(f==0){
                            seeWomen.setEnabled(false);
                            seeMoreWomen.setVisibility(View.GONE);
                            seeWomen.setVisibility(View.GONE);
                        }
                        else{
                            seeWomen.setEnabled(true);
                            seeMoreWomen.setVisibility(View.VISIBLE);
                            seeMoreWomen.setText("See all");
                            seeWomen.setVisibility(View.VISIBLE);
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
    }

    private void addMenGrid() {
        imgNameMen.clear();
        imgUrlMen.clear();

        ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.show();
        Call<TypeList> call=jsonPlaceHolderApi2.getTypes("Mens_Section");
        call.enqueue(new Callback<TypeList>() {
            @Override
            public void onResponse(Call<TypeList> call, Response<TypeList> response) {
                if(response.code()==200){
                    TypeList serviceList=response.body();
                    List<String> list=serviceList.getTypeList();
                    int z=0,f=0;
                    if(list.size()!=0){
                        for(String item:list){
                            if(z<6){
                                imgNameMen.add(item);
                                item=item.replaceAll(" ","_");
                                //Log.d("item",item);
                                imgUrlMen.add(imagebase+"Mens_Section"+item);
                                z++;
                            }
                            else {
                                f=1;
                                imgNameMen1.add(item);
                                item = item.replaceAll(" ", "_");
                                //Log.d("item",item);
                                imgUrlMen1.add(imagebase + "Mens_Section" + item);
                            }
                        }
                        if(f==0){
                            seeMen.setEnabled(false);
                            seeMoreMen.setVisibility(View.GONE);
                            seeMen.setVisibility(View.GONE);
                        }
                        else{
                            seeMen.setEnabled(true);
                            seeMoreMen.setVisibility(View.VISIBLE);
                            seeMen.setVisibility(View.VISIBLE);
                            seeMoreMen.setText("See all");
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
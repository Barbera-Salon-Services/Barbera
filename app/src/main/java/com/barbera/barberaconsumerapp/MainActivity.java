package com.barbera.barberaconsumerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.barbera.barberaconsumerapp.Bookings.BookingsActivity;
import com.barbera.barberaconsumerapp.LighteningDeals.LighteningDeal;
import com.barbera.barberaconsumerapp.Profile.ProfileActivity;
import com.barbera.barberaconsumerapp.Service50.Service_50;
import com.barbera.barberaconsumerapp.Utils.ServiceItem;
import com.barbera.barberaconsumerapp.Utils.ServiceList;
import com.barbera.barberaconsumerapp.network_aws.JsonPlaceHolderApi2;
import com.barbera.barberaconsumerapp.network_aws.RetrofitClientInstanceService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.denzcoskun.imageslider.ImageSlider;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import eu.dkaratzas.android.inapp.update.Constants;
import eu.dkaratzas.android.inapp.update.InAppUpdateManager;
import eu.dkaratzas.android.inapp.update.InAppUpdateStatus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements InAppUpdateManager.InAppUpdateHandler {
    private ImageView Cart;
    public static CartAdapter cartAdapter;
    private ImageSlider imageSlider;
    private InAppUpdateManager inAppUpdateManager;
    private LinearLayoutManager layoutManager;
    public static List<ServiceItem> menHorizontalserviceList=new ArrayList<ServiceItem>();
    private RecyclerView MenTrendRecyclerView;
    private MenHorizontalAdapter adapter;
    public static LatLng center3,center4,center5,center6,center7,center8,center9,center10,center11,center12;
    public static double  radius3,radius4,radius7,radius5,radius6,radius8,radius9,radius10,radius11,radius12;
    public static List<ServiceItem> womenHorizontalserviceList=new ArrayList<ServiceItem>();
    public MenHorizontalAdapter womenadapter;
    public RecyclerView WoMenTrendRecyclerView;
    private LinearLayoutManager womenlayoutManager;
    private ImageView weddingSection;
    private ImageView off;
    private static TextView NumberOnCartMain;
    private CardView Light;
    private RelativeLayout Offers;
    private RelativeLayout Service50;
    private String isRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigation);
        Cart=(ImageView)findViewById(R.id.cart);
        Light = findViewById(R.id.Light);
        Offers  =findViewById(R.id.Offers);
        Service50 = findViewById(R.id.Service50);
        cartAdapter=new CartAdapter(this);
        Button menSalon=(Button) findViewById(R.id.view_men_salon);
        Button womenSalon=(Button) findViewById(R.id.view_women_salon);
        imageSlider=(ImageSlider)findViewById(R.id.slider_view);
        layoutManager=new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        MenTrendRecyclerView = (RecyclerView) findViewById(R.id.men_horizontal_view);
        MenTrendRecyclerView.setLayoutManager(layoutManager);
        adapter=new MenHorizontalAdapter(menHorizontalserviceList,MainActivity.this,0);
        MenTrendRecyclerView.setAdapter(adapter);
        womenadapter=new MenHorizontalAdapter(womenHorizontalserviceList,MainActivity.this,1);
        WoMenTrendRecyclerView = (RecyclerView) findViewById(R.id.women_horizontal_view);
        womenlayoutManager=new LinearLayoutManager(getApplicationContext());
        womenlayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        WoMenTrendRecyclerView.setLayoutManager(womenlayoutManager);
        WoMenTrendRecyclerView.setAdapter(womenadapter);
        ImageView referMain=(ImageView)findViewById(R.id.refer);
        weddingSection =(ImageView)findViewById(R.id.wedding_picture);
        NumberOnCartMain=(TextView)findViewById(R.id.numberOfCartMain);

        SharedPreferences preferences=getSharedPreferences("Token",MODE_PRIVATE);
        isRegistered = preferences.getString("token","no");

        referMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ReferAndEarn.class));
            }
        });

        Light.setOnClickListener(v -> {
            Intent intent=new Intent(MainActivity.this, LighteningDeal.class);
            startActivity(intent);
        });

        Offers.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Offers.class);
            startActivity(intent);
        });

        Service50.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Service_50.class);
            startActivity(intent);
        });

        menSalon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,CategoryActivity.class);
                intent.putExtra("Type","Men\'s Salon");
                startActivity(intent);
            }
        });
        womenSalon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,CategoryActivity.class);
                intent.putExtra("Type","Women\'s Salon");
                startActivity(intent);
            }
        });

        FirebaseFirestore.getInstance().collection("AppData").document("Offers").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            Glide.with(getApplicationContext()).load(task.getResult().get("Offer1"))
                                    .apply(new RequestOptions().placeholder(R.drawable.log)).into(weddingSection);
                        }
                    }
                });
        weddingSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),WeddingPreActivity.class));
            }
        });



        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Successful";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }
                    }
                });


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


        Cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRegistered.equals("no")){
                    Toast.makeText(getApplicationContext(),"You Must Log In to continue",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),SecondScreen.class));
                }

                else
                startActivity(new Intent(MainActivity.this,CartActivity.class));
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

    private void loadImageSlider() {
        imageSlider.setImageList(dbQueries.slideModelList,true);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(menHorizontalserviceList.size()==0 && womenHorizontalserviceList.size()==0){
            Retrofit retrofit = RetrofitClientInstanceService.getRetrofitInstance();
            JsonPlaceHolderApi2 jsonPlaceHolderApi2=retrofit.create(JsonPlaceHolderApi2.class);
            Call<ServiceList> call =jsonPlaceHolderApi2.getTrending("Bearer "+isRegistered);
            final ProgressBar menBar=(ProgressBar)findViewById(R.id.bar_at_men_horizontal);
            menBar.setVisibility(View.VISIBLE);
            final ProgressBar womenBar=(ProgressBar)findViewById(R.id.bar_at_women_horizontal);
            womenBar.setVisibility(View.VISIBLE);
            call.enqueue(new Callback<ServiceList>() {
                @Override
                public void onResponse(Call<ServiceList> call, Response<ServiceList> response) {
                    if(response.code()==200){
                        ServiceList serviceList=response.body();
                        List<ServiceItem> list=serviceList.getServices();
                        if(list.size()==0){
                            Toast.makeText(getApplicationContext(),"No trending services",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            for(ServiceItem serviceItem :list){
                                if(serviceItem.getGender().equals("male")){
                                    menHorizontalserviceList.add(new ServiceItem(serviceItem.getName(),serviceItem.getPrice(),serviceItem.getTime()
                                            ,serviceItem.getDetail(),serviceItem.getCutprice(),serviceItem.getGender(),serviceItem.getType(),
                                            serviceItem.isDod(),serviceItem.getId(),serviceItem.isTrend(),serviceItem.getSubtype(),serviceItem.getImage()));
                                }
                                else{
                                    womenHorizontalserviceList.add(new ServiceItem(serviceItem.getName(),serviceItem.getPrice(),serviceItem.getTime()
                                            ,serviceItem.getDetail(),serviceItem.getCutprice(),serviceItem.getGender(),serviceItem.getType(),
                                            serviceItem.isDod(),serviceItem.getId(),serviceItem.isTrend(),serviceItem.getSubtype(),serviceItem.getImage()));
                                }
                            }
                            adapter.notifyDataSetChanged();
                            menBar.setVisibility(View.INVISIBLE);
                            womenadapter.notifyDataSetChanged();
                            womenBar.setVisibility(View.INVISIBLE);
                        }

                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Could not load salon",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ServiceList> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }

//        if (FirebaseAuth.getInstance().getCurrentUser()!=null&&dbQueries.cartList.size() == 0) {
//            dbQueries.loadCartList();
//        }
//        if(dbQueries.slideModelList.size()==0)
//            dbQueries.loadslideModelList();
//        loadImageSlider();
        loadNumberOnCart();
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
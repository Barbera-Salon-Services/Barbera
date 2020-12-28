package com.barbera.barberaconsumerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import eu.dkaratzas.android.inapp.update.Constants;
import eu.dkaratzas.android.inapp.update.InAppUpdateManager;
import eu.dkaratzas.android.inapp.update.InAppUpdateStatus;

public class MainActivity extends AppCompatActivity implements InAppUpdateManager.InAppUpdateHandler {
    private CardView menSalon;
    private CardView womenSalon;
    private ImageView Cart;
    public static CartAdapter cartAdapter;
    private ImageSlider imageSlider;
    private InAppUpdateManager inAppUpdateManager;
    private LinearLayoutManager layoutManager;
    public static List<Service> menHorizontalserviceList=new ArrayList<Service>();
    private RecyclerView MenTrendRecyclerView;
    private MenHorizontalAdapter adapter;
    public static List<Service> womenHorizontalserviceList=new ArrayList<Service>();
    public MenHorizontalAdapter womenadapter;
    public RecyclerView WoMenTrendRecyclerView;
    private LinearLayoutManager womenlayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigation);
        Cart=(ImageView)findViewById(R.id.cart);
        cartAdapter=new CartAdapter();
        RelativeLayout menSalon=(RelativeLayout)findViewById(R.id.explore_men_salon);
        RelativeLayout womenSalon=(RelativeLayout)findViewById(R.id.explore_women_salon);
        imageSlider=(ImageSlider)findViewById(R.id.slider_view);
        layoutManager=new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        MenTrendRecyclerView = (RecyclerView) findViewById(R.id.men_horizontal_view);
        MenTrendRecyclerView.setLayoutManager(layoutManager);
        adapter=new MenHorizontalAdapter(menHorizontalserviceList);
        MenTrendRecyclerView.setAdapter(adapter);
        womenadapter=new MenHorizontalAdapter(womenHorizontalserviceList);
        WoMenTrendRecyclerView = (RecyclerView) findViewById(R.id.women_horizontal_view);
        womenlayoutManager=new LinearLayoutManager(getApplicationContext());
        womenlayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        WoMenTrendRecyclerView.setLayoutManager(womenlayoutManager);
        WoMenTrendRecyclerView.setAdapter(womenadapter);
        ImageView referMain=(ImageView)findViewById(R.id.refer);

        referMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ReferAndEarn.class));
            }
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
                        if(FirebaseAuth.getInstance().getCurrentUser()==null){
                            Toast.makeText(getApplicationContext(),"You Must Log In to continue",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(),SecondScreen.class));
                            finish();
                        }
                        else {
                            startActivity(new Intent(getApplicationContext(), BookingsActivity.class));
                            overridePendingTransition(0, 0);
                            finish();
                        }
                        return true;
                    case R.id. profile:
                        if(FirebaseAuth.getInstance().getCurrentUser()==null){
                            Toast.makeText(getApplicationContext(),"You Must Log In to continue",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(),SecondScreen.class));
                            finish();
                        }
                        else {
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                            overridePendingTransition(0, 0);
                            finish();
                        }
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
                if(FirebaseAuth.getInstance().getCurrentUser()==null){
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

    private void loadImageSlider() {
        imageSlider.setImageList(dbQueries.slideModelList,true);
    }

    private void loadWomenTrendingList() {
        final ProgressBar womenBar=(ProgressBar)findViewById(R.id.bar_at_women_horizontal);
        womenBar.setVisibility(View.VISIBLE);
        FirebaseFirestore.getInstance().collection("AppData").document("TrendingWomen").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            for(long i=1;i<=(long)task.getResult().get("trending");i++){
                                String documentName=task.getResult().get("trending_"+i).toString();
                                FirebaseFirestore.getInstance().collection("Women\'s Salon").document(documentName).get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if(task.isSuccessful()){
                                                    DocumentSnapshot documentSnapshot=task.getResult();
                                                    womenHorizontalserviceList.add(new Service(documentSnapshot.get("icon").toString(),
                                                            documentSnapshot.get("Service_title").toString(),
                                                            documentSnapshot.get("price").toString(), documentSnapshot.getId(),
                                                            documentSnapshot.get("type").toString(),documentSnapshot.get("cut_price").toString()));
                                                    womenadapter.notifyDataSetChanged();
                                                    womenBar.setVisibility(View.INVISIBLE);

                                                }
                                            }
                                        });
                            }
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadImageSlider();
        if(menHorizontalserviceList.size()==0)
            loadMenTrendingList();
        if(womenHorizontalserviceList.size()==0)
            loadWomenTrendingList();
    }

    private void loadMenTrendingList() {
        final ProgressBar menBar=(ProgressBar)findViewById(R.id.bar_at_men_horizontal);
        menBar.setVisibility(View.VISIBLE);
        FirebaseFirestore.getInstance().collection("AppData").document("TrendingMen").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            for(long i=1;i<=(long)task.getResult().get("trending");i++){
                                String documentName=task.getResult().get("trending_"+i).toString();
                                FirebaseFirestore.getInstance().collection("Men\'s Salon").document(documentName).get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if(task.isSuccessful()){
                                                    DocumentSnapshot documentSnapshot=task.getResult();
                                                    menHorizontalserviceList.add(new Service(documentSnapshot.get("icon").toString(),
                                                            documentSnapshot.get("Service_title").toString(),
                                                            documentSnapshot.get("price").toString(), documentSnapshot.getId(),
                                                            documentSnapshot.get("type").toString(),documentSnapshot.get("cut_price").toString()));
                                                    adapter.notifyDataSetChanged();
                                                    menBar.setVisibility(View.INVISIBLE);

                                                }
                                            }
                                        });
                            }
                        }
                    }
                });

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
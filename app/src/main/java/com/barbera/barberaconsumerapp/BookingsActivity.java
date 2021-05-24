package com.barbera.barberaconsumerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BookingsActivity extends AppCompatActivity {
    private List<BookingModel> bookingActivityList=new ArrayList<>();
    private RecyclerView BookinglistView;
    private ProgressBar progressBarONBookingActivity;
    private static RelativeLayout emptyLayout;
    public static boolean checked=false;
    private SharedPreferences sharedPreferences;
    public static BookingActivityAdapter bookingActivityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);
        sharedPreferences=getSharedPreferences("UserInfo",MODE_PRIVATE);


        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigation);
       // ImageView cart=(ImageView)findViewById(R.id.cartONBooking);
        BookinglistView=findViewById(R.id.BookingListView);
        BookinglistView.setHasFixedSize(true);
        BookinglistView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        progressBarONBookingActivity=(ProgressBar)findViewById(R.id.progressBarOnBookingActivity);
        emptyLayout=(RelativeLayout)findViewById(R.id.empty_booking_layout);
        Button newBooking=(Button)findViewById(R.id.add_a_booking);
        bookingActivityAdapter=new BookingActivityAdapter(bookingActivityList,getApplicationContext(),getSupportFragmentManager());

        newBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookingsActivity.this,MainActivity.class));
                finish();
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.booking);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id. booking:
                        return true;
                    case R.id. profile:
                        startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                }
                return false;
            }
        });

        if(!checked&&FirebaseAuth.getInstance().getCurrentUser()!=null){
            progressBarONBookingActivity.setVisibility(View.VISIBLE);
            FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("Bookings").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                                    String status = "";
                                    try {
                                        status = documentSnapshot.get("status").toString();
                                    } catch (Exception ignored) {
                                        status = "pending";
                                    }
                                    try {
                                        bookingActivityList.add(new BookingModel(documentSnapshot.get("service").toString(), documentSnapshot.get("total_amount").toString(),
                                                documentSnapshot.get("date").toString(), documentSnapshot.get("time").toString(), documentSnapshot.get("address").toString(),
                                                documentSnapshot.getId(), status, documentSnapshot.get("total_time").toString(), documentSnapshot.get("randomId").toString()));
                                    } catch (Exception ignored) {

                                    }
                                    FirebaseFirestore.getInstance().collection("Manual booking").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .collection("Weekly booking").get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if(task.isSuccessful()){
                                                        for(QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())){
                                                            String status = "";
                                                            try{
                                                                status =documentSnapshot.get("status").toString();
                                                            }catch (Exception ignored){
                                                                status ="pending";
                                                            }
                                                            try{
                                                                bookingActivityList.add(new BookingModel(documentSnapshot.get("service").toString()+"(Weekly)",documentSnapshot.get("total_amount").toString(),
                                                                        documentSnapshot.get("date").toString(),documentSnapshot.get("time").toString(),documentSnapshot.get("address").toString(),
                                                                        documentSnapshot.getId(),status,documentSnapshot.get("total_time").toString(),documentSnapshot.get("randomId").toString()));
                                                            }catch (Exception ignored){

                                                            }
                                                        }
                                                    }
                                                    else{
                                                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                    FirebaseFirestore.getInstance().collection("Manual booking").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .collection("Monthly booking").get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if(task.isSuccessful()){
                                                        for(QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())){
                                                            String status = "";
                                                            try{
                                                                status =documentSnapshot.get("status").toString();
                                                            }catch (Exception ignored){
                                                                status ="pending";
                                                            }
                                                            try{
                                                                bookingActivityList.add(new BookingModel(documentSnapshot.get("service").toString()+"(Monthly)",documentSnapshot.get("total_amount").toString(),
                                                                        documentSnapshot.get("date").toString(),documentSnapshot.get("time").toString(),documentSnapshot.get("address").toString(),
                                                                        documentSnapshot.getId(),status,documentSnapshot.get("total_time").toString(),documentSnapshot.get("randomId").toString()));
                                                            }catch (Exception ignored){

                                                            }
                                                        }
                                                    }
                                                    else{
                                                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                    checked=true;
                                    BookinglistView.setAdapter(bookingActivityAdapter);
                                    if(bookingActivityList.size()==0){
                                        //Toast.makeText(getApplicationContext(),"No Bookings Yet",Toast.LENGTH_LONG).show();
                                        BookinglistView.setVisibility(View.INVISIBLE);
                                        emptyLayout.setVisibility(View.VISIBLE);
                                    }
                                    progressBarONBookingActivity.setVisibility(View.INVISIBLE);
                                }
                            }
                            else
                                Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });

        }
        if(checked&&bookingActivityList.size()!=0){
            BookinglistView.setVisibility(View.VISIBLE);
            BookinglistView.setAdapter(bookingActivityAdapter);
        }
        else if(checked&&bookingActivityList.size()==0){
            BookinglistView.setVisibility(View.INVISIBLE);
            emptyLayout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        overridePendingTransition(0,0);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(new Intent(getApplicationContext(),BookingsActivity.class));
    }
}
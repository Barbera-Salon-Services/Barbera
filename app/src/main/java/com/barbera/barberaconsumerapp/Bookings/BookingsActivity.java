package com.barbera.barberaconsumerapp.Bookings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.barbera.barberaconsumerapp.HomeActivity;
import com.barbera.barberaconsumerapp.MainActivity;
import com.barbera.barberaconsumerapp.Profile.ProfileActivity;
import com.barbera.barberaconsumerapp.R;
import com.barbera.barberaconsumerapp.network_aws.JsonPlaceHolderApi2;
import com.barbera.barberaconsumerapp.network_aws.RetrofitClientInstanceBooking;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BookingsActivity extends AppCompatActivity {
    private List<BookingModel> bookingActivityList=new ArrayList<>();
    private RecyclerView BookinglistView;
    private ProgressBar progressBarONBookingActivity;
    private static RelativeLayout emptyLayout;
    public static boolean checked=false;
    private String token;
    private SharedPreferences sharedPreferences;
    public static BookingActivityAdapter bookingActivityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);
        sharedPreferences=getSharedPreferences("UserInfo",MODE_PRIVATE);

        Retrofit retrofit = RetrofitClientInstanceBooking.getRetrofitInstance();
        JsonPlaceHolderApi2 jsonPlaceHolderApi2=retrofit.create(JsonPlaceHolderApi2.class);
        SharedPreferences preferences = getSharedPreferences("Token", MODE_PRIVATE);
        token = preferences.getString("token", "no");

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
                startActivity(new Intent(BookingsActivity.this, MainActivity.class));
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
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                }
                return false;
            }
        });

        if(!checked&&!token.equals("no")){
            //bookingActivityList=new ArrayList<>();
            progressBarONBookingActivity.setVisibility(View.VISIBLE);
            Call<BookingList> call=jsonPlaceHolderApi2.getBookings("Bearer "+token);
            call.enqueue(new Callback<BookingList>() {
                @Override
                public void onResponse(Call<BookingList> call, Response<BookingList> response) {

                    if(response.code()==200){
                        BookingList bookingList=response.body();
                        List<BookingItem> list=bookingList.getList();
                        if(list.size()==0){
                            BookinglistView.setVisibility(View.INVISIBLE);
                            emptyLayout.setVisibility(View.VISIBLE);
                            progressBarONBookingActivity.setVisibility(View.INVISIBLE);
                        }
                        else {
                            //Log.d("List","IN");
                            int i = 0, amount = 0;
                            List<String> idList=new ArrayList<>();
                            String summary = "", date = "", slot = "", timestamp = "",id="",status="",barberName="",barberPhone="";
                            double dist=0;
                            for (BookingItem item : list) {
                                if (i == 0) {
                                    date = item.getDate();
                                    slot = item.getSlot();
                                    String name = item.getService().getName();
                                    String gender = item.getService().getGender();
                                    int price = item.getService().getPrice();
                                    int quantity=item.getQuantity();
                                    summary += "(" + gender + ") " + name + "   Rs: " + price + "  ("+quantity+")"+"\n";
                                    amount += (item.getQuantity()*item.getService().getPrice());
                                    timestamp += item.getTimestamp();
                                    id=item.getBarberItem().getBarberid();
                                    idList.add(item.getServiceId());
                                    status=item.getStatus();
                                    barberName=item.getBarberItem().getName();
                                    barberPhone=item.getBarberItem().getPhone();
                                    dist=item.getBarberItem().getDistance();
                                    //Log.d("id",idList.size()+"");
                                    i++;
                                } else {
                                    if (item.getTimestamp().equals(timestamp)) {
                                        String name = item.getService().getName();
                                        String gender = item.getService().getGender();
                                        int price = item.getService().getPrice();
                                        int quantity=item.getQuantity();
                                        summary += "(" + gender + ") " + name + "   Rs: " + price +"  ("+quantity+")"+"\n";
                                        amount += (item.getQuantity()*item.getService().getPrice());
                                        date = item.getDate();
                                        slot = item.getSlot();
                                        id=item.getBarberItem().getBarberid();
                                        idList.add(item.getService().getId());
                                    } else {
                                        //Log.d("id",idList.size()+"");
                                        bookingActivityList.add(new BookingModel(summary, amount, date, slot,id,idList,status,barberName,barberPhone,dist));
                                        date = item.getDate();
                                        slot = item.getSlot();
                                        status=item.getStatus();
                                        summary = "";
                                        String name = item.getService().getName();
                                        String gender = item.getService().getGender();
                                        int price = item.getService().getPrice();
                                        int quantity=item.getQuantity();
                                        summary += "(" + gender + ") " + name + "   Rs: " + price +"  ("+quantity+")"+ "\n";
                                        amount = 0;
                                        amount += (item.getQuantity()*item.getService().getPrice());
                                        timestamp = "";
                                        timestamp += item.getTimestamp();
                                        id=item.getBarberItem().getBarberid();
                                        idList=new ArrayList<>();
                                        idList.add(item.getServiceId());
                                        barberName=item.getBarberItem().getName();
                                        barberPhone=item.getBarberItem().getPhone();
                                        dist=item.getBarberItem().getDistance();
                                    }
                                }
                            }
                            //Log.d("last", idList.size()+"");
//                            for(String s:idList){
//                                Log.d("qw",s);
//                            }
                            bookingActivityList.add(new BookingModel(summary, amount, date, slot,id,idList,status,barberName,barberPhone,dist));
//                            for (BookingModel item : bookingActivityList) {
//                                Log.d("item", item.getDate() + " " + item.getTime());
//                            }
                            BookinglistView.setAdapter(bookingActivityAdapter);
                            progressBarONBookingActivity.setVisibility(View.INVISIBLE);
                        }

                    }
                    else{
                        progressBarONBookingActivity.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(),"Could not get bookings",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<BookingList> call, Throwable t) {
                    progressBarONBookingActivity.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
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
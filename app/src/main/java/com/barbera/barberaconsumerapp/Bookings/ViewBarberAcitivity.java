package com.barbera.barberaconsumerapp.Bookings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.barbera.barberaconsumerapp.R;
import com.barbera.barberaconsumerapp.network_aws.JsonPlaceHolderApi2;
import com.barbera.barberaconsumerapp.network_aws.RetrofitClientInstanceBooking;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ViewBarberAcitivity extends AppCompatActivity {
    private List<BarberItem> barberList=new ArrayList<>();
    private String token;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private BarberAdapter adapter;
    private ArrayList<String> sidlist;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_barber_acitivity);

        Intent intent=getIntent();
        String dat=intent.getStringExtra("date");
        int slot=intent.getIntExtra("slot",0);
        String amount=intent.getStringExtra("Booking Amount");
        String summary=intent.getStringExtra("Order Summary");
        sidlist=intent.getStringArrayListExtra("sidlist");

        recyclerView=findViewById(R.id.barber_recycler_view);
        adapter=new BarberAdapter(barberList,this,dat,slot,amount,summary,sidlist);
        manager= new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        Retrofit retrofit = RetrofitClientInstanceBooking.getRetrofitInstance();
        JsonPlaceHolderApi2 jsonPlaceHolderApi2=retrofit.create(JsonPlaceHolderApi2.class);
        SharedPreferences preferences = getSharedPreferences("Token", MODE_PRIVATE);
        token = preferences.getString("token", "no");

        Call<BarberList> call=jsonPlaceHolderApi2.getBarbers(dat,slot,"Bearer "+token);
        call.enqueue(new Callback<BarberList>() {
            @Override
            public void onResponse(Call<BarberList> call, Response<BarberList> response) {
                if(response.code()==200){
                    BarberList list=response.body();
                    List<BarberItem> barberItemList=list.getList();
                    if(barberItemList.size()==0){
                        Toast.makeText(getApplicationContext(),"No barber available for this date and slot",Toast.LENGTH_LONG).show();
                    }
                    else{
                        for(BarberItem item:barberItemList){
                            barberList.add(item);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Could not get barber list",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BarberList> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }
}
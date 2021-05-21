package com.barbera.barberaconsumerapp.LighteningDeals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.barbera.barberaconsumerapp.BookingPage;
import com.barbera.barberaconsumerapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class LighteningDeal extends AppCompatActivity {
    private  RecyclerView men;
    private RecyclerView women;
    private List<LightenDealItem> menList;
    private  List<LightenDealItem> womenList;
    private LighteningDealAdapter menAdapter;
    private LighteningDealAdapter womenAdapter;
    private TextView m;
    private TextView w;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lightening_deal);

        m=findViewById(R.id.men);
        w= findViewById(R.id.women);

        men = findViewById(R.id.new_men_recycler_view);
        women = findViewById(R.id.new_women_recycler_view);

        men.setHasFixedSize(true);
        men.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        menList = new ArrayList<>();
        womenList = new ArrayList<>();

        women.setHasFixedSize(true);
        women.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        menAdapter = new LighteningDealAdapter(menList,getApplicationContext(),0);
        womenAdapter  = new LighteningDealAdapter(womenList, getApplicationContext(),1);

        fetchDeals();
    }

    private void fetchDeals() {
        ProgressDialog progressDialog = new ProgressDialog(LighteningDeal.this);
        progressDialog.setMessage("Hold on for a moment...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        FirebaseFirestore.getInstance().collection("Men's Salon").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                try {
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        if (documentSnapshot.get("isDealsOfDay").toString().equals("yes")) {
                            menList.add(new LightenDealItem(documentSnapshot.get("Service_title").toString(), documentSnapshot.get("icon").toString()
                                    , Integer.parseInt(documentSnapshot.get("Time").toString()), Integer.parseInt(documentSnapshot.get("price").toString()),
                                    Integer.parseInt(documentSnapshot.get("discount").toString())));
                        }
                    }
                    if(menList.size()!=0)
                        men.setAdapter(menAdapter);
                    else
                        m.setVisibility(View.INVISIBLE);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Something Went Wrong",Toast.LENGTH_SHORT).show();
                }
            }
            FirebaseFirestore.getInstance().collection("Women's Salon").get().addOnCompleteListener(task1 -> {
                if(task1.isSuccessful()){
                    try{
                        for(DocumentSnapshot documentSnapshot : task1.getResult()){
                            if (documentSnapshot.get("isDealsOfDay").toString().equals("yes")) {
                                womenList.add(new LightenDealItem(documentSnapshot.get("Service_title").toString(), documentSnapshot.get("icon").toString()
                                        , Integer.parseInt(documentSnapshot.get("Time").toString()), Integer.parseInt(documentSnapshot.get("price").toString()),
                                        Integer.parseInt(documentSnapshot.get("discount").toString())));
                            }
                        }
                        if(womenList.size()!= 0)
                            women.setAdapter(womenAdapter);
                        else
                            w.setVisibility(View.INVISIBLE);
                        progressDialog.dismiss();
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),"Something Went Wrong",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                }
            });
        });
    }
}
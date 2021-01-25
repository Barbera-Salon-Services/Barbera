package com.barbera.barberaconsumerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class WeddingActivity2 extends AppCompatActivity {
    private WeddingAdapter2 mehandiAdapter,makeupAdapter;
    private String weddingType;
    private TextView weddingHeading;
    public static ProgressBar progressBarOnServiceList;
    private static List<WeddingModel> mehndiList = new ArrayList<>();
    private static List<WeddingModel> makeupList = new ArrayList<>();
    public static Button addToCart;
    public static Button bookNow;
    private ListView listView;
    private String CategoryIMage;
    private ImageView image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_service_activity);

        Intent intent=getIntent();
        weddingType=intent.getStringExtra("Type");
        CategoryIMage=intent.getStringExtra("CategoryIMage");
        WeddingActivity2.progressBarOnServiceList=(ProgressBar)findViewById(R.id.new_service_progress_bar);
        weddingHeading=(TextView)findViewById(R.id.new_service_title);
        addToCart=(Button)findViewById(R.id.new_service_add_to_cart);
        bookNow=(Button)findViewById(R.id.new_service_book_now_button);
        image=(ImageView)findViewById(R.id.new_service_image);
        listView=(ListView) findViewById(R.id.new_service_recycler_view);
        ImageView cart=(ImageView)findViewById(R.id.new_service_cart);

        weddingHeading.setText(weddingType);

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FirebaseAuth.getInstance().getCurrentUser()==null){
                    Toast.makeText(getApplicationContext(),"You Must Log In to continue",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                }
                else {
                    startActivity(new Intent(WeddingActivity2.this, CartActivity.class));
                }
            }
        });
        if(weddingType.equals("Mehndi")&&mehndiList.size()==0)
            loadMehndiList();
        else if(weddingType.equals("Makeup")&&makeupList.size()==0)
            loadMakeupList();
        else if(weddingType.equals("Mehndi")&&mehndiList.size()!=0) {
            listView.setAdapter(mehandiAdapter);
        }
        else if(weddingType.equals("Makeup")&&makeupList.size()!=0) {
            listView.setAdapter(makeupAdapter);
        }

    }

    private void loadMehndiList(){
        WeddingActivity2.progressBarOnServiceList.setVisibility(View.VISIBLE);
        FirebaseFirestore.getInstance().collection("Mehandi").orderBy("price").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot:task.getResult()) {
                                mehndiList.add(new WeddingModel(documentSnapshot.getId(), null, documentSnapshot.get("price").toString(),
                                        "Mehandi", documentSnapshot.get("cut_price").toString(), null));
                            }
                            listView.setAdapter(mehandiAdapter);
                            WeddingActivity2.progressBarOnServiceList.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }
    private void loadMakeupList(){
        WeddingActivity2.progressBarOnServiceList.setVisibility(View.VISIBLE);
        FirebaseFirestore.getInstance().collection("WeddingMakeup").orderBy("price").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                makeupList.add(new WeddingModel(documentSnapshot.getId(), null, documentSnapshot.get("price").toString(),
                                        "WeddingMakeup", documentSnapshot.get("cut_price").toString(), null));
                            }
                            listView.setAdapter(makeupAdapter);
                            WeddingActivity2.progressBarOnServiceList.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }
}

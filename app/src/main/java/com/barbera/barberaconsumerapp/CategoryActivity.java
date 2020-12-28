package com.barbera.barberaconsumerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private GridView gridView;
    public static List<CategoryDesign> menCategoryList=new ArrayList<>();
    public static List<CategoryDesign> womenCategoryList=new ArrayList<>();
    private String type;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_men_parlour);

        Intent intent=getIntent();
        type=intent.getStringExtra("Type");
        TextView title = (TextView) findViewById(R.id.salon_title);
        ImageView cart = (ImageView) findViewById(R.id.cartOnParlour);
        progressBar=(ProgressBar)findViewById(R.id.progressBarOnMenSalon);
        gridView=(GridView)findViewById(R.id.RootView);
        final CategoryAdapter adapter = new CategoryAdapter(menCategoryList);
        final CategoryAdapter womenadapter=new CategoryAdapter(womenCategoryList);


        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FirebaseAuth.getInstance().getCurrentUser()==null){
                    Toast.makeText(getApplicationContext(),"You Must Log In to continue",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                }
                else {
                    startActivity(new Intent(CategoryActivity.this, CartActivity.class));
                }
            }
        });

        title.setText(type);
        gridView.setNumColumns(3);

        if(type.equals("Men\'s Salon")&&menCategoryList.size()==0){
            progressBar.setVisibility(View.VISIBLE);
            FirebaseFirestore.getInstance().collection("MenCategory").orderBy("key").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
                                   menCategoryList.add(new CategoryDesign(documentSnapshot.get("Category_icon").toString(),documentSnapshot.getId()));
                                }
                                gridView.setAdapter(adapter);
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
        }
        else if(type.equals("Women\'s Salon")&&womenCategoryList.size()==0){
            progressBar.setVisibility(View.VISIBLE);
            FirebaseFirestore.getInstance().collection("WomenCategory").orderBy("key").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
                                    womenCategoryList.add(new CategoryDesign(documentSnapshot.get("Category_icon").toString(),documentSnapshot.getId()));
                                }
                                gridView.setAdapter(womenadapter);
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        }
                    });

        }
        else if(type.equals("Men\'s Salon")&&menCategoryList.size()!=0){
            gridView.setAdapter(adapter);
        }
        else if(type.equals("Women\'s Salon")&&womenCategoryList.size()!=0){
            gridView.setAdapter(womenadapter);
        }
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                if(type.equals("Men\'s Salon")) {
                    intent = new Intent(CategoryActivity.this, MenParlourActivity.class);
                    intent.putExtra("Category", menCategoryList.get(position).getCategoryName());
                }
                else{
                    intent = new Intent(CategoryActivity.this, WomenParlourActivity.class);
                    intent.putExtra("Category", womenCategoryList.get(position).getCategoryName());

                }
                startActivity(intent);
            }
        });
    }
}
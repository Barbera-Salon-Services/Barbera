package com.barbera.barberaconsumerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.barbera.barberaconsumerapp.Utils.ServiceItem;
import com.barbera.barberaconsumerapp.Utils.ServiceList;
import com.barbera.barberaconsumerapp.Utils.TypeList;
import com.barbera.barberaconsumerapp.network_aws.JsonPlaceHolderApi2;
import com.barbera.barberaconsumerapp.network_aws.RetrofitClientInstance2;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoryActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private GridView gridView;
    public static List<String> menCategoryList=new ArrayList<>();
    public static List<String> womenCategoryList=new ArrayList<>();
    private String type,token;
    private static TextView numberCartCategory;

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
        numberCartCategory=(TextView)findViewById(R.id.numberOfCartCategory);

        Retrofit retrofit = RetrofitClientInstance2.getRetrofitInstance();
        JsonPlaceHolderApi2 jsonPlaceHolderApi2=retrofit.create(JsonPlaceHolderApi2.class);
        SharedPreferences preferences=getSharedPreferences("Token",MODE_PRIVATE);
        token = preferences.getString("token","no");

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FirebaseAuth.getInstance().getCurrentUser()==null){
                    Toast.makeText(getApplicationContext(),"You Must Log In to continue",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),ActivityPhoneVerification.class));
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
            Call<TypeList> call=jsonPlaceHolderApi2.getTypes("male",token);
            call.enqueue(new Callback<TypeList>() {
                @Override
                public void onResponse(Call<TypeList> call, Response<TypeList> response) {
                    if(response.code()==200){
                        TypeList serviceList=response.body();
                        List<String> list=serviceList.getTypeList();
                        for(String item:list){
                            menCategoryList.add(item);
                        }
                        gridView.setAdapter(adapter);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Could not get services",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<TypeList> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if(type.equals("Women\'s Salon")&&womenCategoryList.size()==0){
            progressBar.setVisibility(View.VISIBLE);
            Call<TypeList> call=jsonPlaceHolderApi2.getTypes("female",token);
            call.enqueue(new Callback<TypeList>() {
                @Override
                public void onResponse(Call<TypeList> call, Response<TypeList> response) {
                    if(response.code()==200){
                        TypeList serviceList=response.body();
                        List<String> list=serviceList.getTypeList();
                        for(String item:list){
                            womenCategoryList.add(item);
                        }
                        gridView.setAdapter(womenadapter);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Could not get services",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<TypeList> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
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
                if(type.equals("Men\'s Salon")) {
                    Call<TypeList> call=jsonPlaceHolderApi2.getSubTypes("male",new ServiceItem(null,null,null,null,null,null,menCategoryList.get(position),
                            false,null,false,null),token);
                    call.enqueue(new Callback<TypeList>() {
                        @Override
                        public void onResponse(Call<TypeList> call, Response<TypeList> response) {
                            if(response.code()==200){
                                TypeList serviceList=response.body();
                                List<String> list=serviceList.getTypeList();
                                if(list.size()==0){
                                    Intent intent = new Intent(CategoryActivity.this, ParlourActivity.class);
                                    intent.putExtra("Category", menCategoryList.get(position));
                                    //intent.putExtra("CategoryIMage",menCategoryList.get(position).getCategoryImage());
                                    intent.putExtra("SalonType","men");
                                    intent.putExtra("Collection","MenCategory");
                                    intent.putExtra("ServiceType",type);
                                    intent.putExtra("SubCategDoc","Null");
                                    startActivity(intent);
                                }
                                else{
                                    Intent intent = new Intent(CategoryActivity.this, SubCategoryActivity.class);
                                    intent.putExtra("Category", menCategoryList.get(position));
                                    //intent.putExtra("CategoryIMage",menCategoryList.get(position).getCategoryImage());
                                    intent.putExtra("SalonType","men");
                                    intent.putExtra("Collection","MenCategory");
                                    intent.putExtra("ServiceType",type);
                                    startActivity(intent);
                                }
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Could not load this service",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<TypeList> call, Throwable t) {
                            Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else{
                    Call<TypeList> call=jsonPlaceHolderApi2.getSubTypes("female",new ServiceItem(null,null,null,null,null,null,womenCategoryList.get(position),
                            false,null,false,null),token);
                    call.enqueue(new Callback<TypeList>() {
                        @Override
                        public void onResponse(Call<TypeList> call, Response<TypeList> response) {
                            if(response.code()==200){
                                TypeList serviceList=response.body();
                                List<String> list=serviceList.getTypeList();
                                if(list.size()==0){
                                    Intent intent = new Intent(CategoryActivity.this, ParlourActivity.class);
                                    intent.putExtra("Category", womenCategoryList.get(position));
                                    //intent.putExtra("CategoryIMage",menCategoryList.get(position).getCategoryImage());
                                    intent.putExtra("SalonType","women");
                                    intent.putExtra("Collection","WomenCategory");
                                    intent.putExtra("ServiceType",type);
                                    intent.putExtra("SubCategDoc","Null");
                                    startActivity(intent);
                                }
                                else{
                                    Intent intent = new Intent(CategoryActivity.this, SubCategoryActivity.class);
                                    intent.putExtra("Category", womenCategoryList.get(position));
                                    //intent.putExtra("CategoryIMage",womenCategoryList.get(position).getCategoryImage());
                                    intent.putExtra("SalonType","women");
                                    intent.putExtra("Collection","WomenCategory");
                                    intent.putExtra("ServiceType",type);
                                    startActivity(intent);
                                }
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Could not load this service",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<TypeList> call, Throwable t) {
                            Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
    }

    public static void loadNumberOnCartCategory(){
        if(FirebaseAuth.getInstance().getCurrentUser()==null)
            numberCartCategory.setText("0");
        else {
            FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("UserData").document("MyCart").get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                //NumberOnCartMain.setText(task.getResult().get("cart_list_size").toString());
                                numberCartCategory.setText(task.getResult().get("cart_list_size").toString());
                                // numberCartParlour.setText(task.getResult().get("cart_list_size").toString());
                            }
                        }
                    });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadNumberOnCartCategory();
    }
}
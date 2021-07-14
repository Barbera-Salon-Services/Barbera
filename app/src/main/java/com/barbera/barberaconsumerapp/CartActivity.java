 package com.barbera.barberaconsumerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.barbera.barberaconsumerapp.Utils.CartItemModel;
import com.barbera.barberaconsumerapp.Utils.CartList;
import com.barbera.barberaconsumerapp.Utils.CartList2;
import com.barbera.barberaconsumerapp.network_aws.JsonPlaceHolderApi2;
import com.barbera.barberaconsumerapp.network_aws.RetrofitClientInstanceCart;
import com.barbera.barberaconsumerapp.network_aws.RetrofitClientInstanceUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CartActivity extends AppCompatActivity {
    public static RecyclerView cartItemRecyclerView;
    public static ProgressBar progressBarMyCart;
    public static TextView total_cart_amount;
    public static int totalAmount=0;
    public static Button continueToBooking;
    public static RelativeLayout emptyCart;
    public static RelativeLayout cartTotalAmtLayout;
    private String token;
    private Button save;
    private JsonPlaceHolderApi2 jsonPlaceHolderApi2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartItemRecyclerView=(RecyclerView) findViewById(R.id.cart_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        cartItemRecyclerView.setLayoutManager(layoutManager);
        progressBarMyCart=(ProgressBar)findViewById(R.id.progressBarOnMyCart);
        total_cart_amount=(TextView)findViewById(R.id.total_cart_amount);
        continueToBooking=(Button)findViewById(R.id.continue_from_cart_button);
        emptyCart=(RelativeLayout)findViewById(R.id.empty_cart);
        Button addInEmptyCart=(Button)findViewById(R.id.add_a_service);
        cartTotalAmtLayout=(RelativeLayout)findViewById(R.id.cart_total_amount_layout);
        //save=findViewById(R.id.save_cart);

        updateCartItemModelList();

        addInEmptyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this,MainActivity.class));
                finish();
            }
        });

//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Retrofit retrofit = RetrofitClientInstanceCart.getRetrofitInstance();
//                JsonPlaceHolderApi2 jsonPlaceHolderApi2 = retrofit.create(JsonPlaceHolderApi2.class);
//                SharedPreferences preferences = getSharedPreferences("Token", MODE_PRIVATE);
//                String token = preferences.getString("token", "no");
//                List<CartItemModel> cartItemModels=new ArrayList<>();
//                for(int i=0;i<dbQueries.cartItemModelList.size();i++){
//                    cartItemModels.add(new CartItemModel(null,null,0,null,dbQueries.cartItemModelList.get(i).getQuantity(),0,dbQueries.cartItemModelList.get(i).getId(),false));
//                }
//                Call<Void> call=jsonPlaceHolderApi2.updateQuantity(new CartList2(cartItemModels),"Bearer "+token);
//                call.enqueue(new Callback<Void>() {
//                    @Override
//                    public void onResponse(Call<Void> call, Response<Void> response) {
//                        if(response.code()==200){
//                            Toast.makeText(getApplicationContext(),"Cart updated",Toast.LENGTH_SHORT).show();
//                        }
//                        else{
//                            Toast.makeText(getApplicationContext(),"Could not update cart",Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    @Override
//                    public void onFailure(Call<Void> call, Throwable t) {
//                        Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
//

    }

    public void updateCartItemModelList(){
        Retrofit retrofit = RetrofitClientInstanceCart.getRetrofitInstance();
        JsonPlaceHolderApi2 jsonPlaceHolderApi2 = retrofit.create(JsonPlaceHolderApi2.class);
        SharedPreferences preferences = getSharedPreferences("Token", MODE_PRIVATE);
        String token = preferences.getString("token", "no");
        //CartActivity.progressBarMyCart.setVisibility(View.VISIBLE);
        dbQueries.cartItemModelList.clear();
        Call<CartList> call= jsonPlaceHolderApi2.getCart("Bearer "+token);
        call.enqueue(new Callback<CartList>() {
            @Override
            public void onResponse(Call<CartList> call, Response<CartList> response) {
                if(response.code()==200){
                    CartList cartList=response.body();
                    int count=cartList.getCount();
                    if(count!=0){
                        List<CartItemModel> list=cartList.getList();
                        for(CartItemModel itemModel:list) {
                            dbQueries.cartItemModelList.add(new CartItemModel(null,itemModel.getServiceName(),itemModel.getServicePrice(),
                                    itemModel.getType(),itemModel.getQuantity(),itemModel.getTime(),itemModel.getId(),false));
                        }
                        //MainActivity.cartAdapter.notifyDataSetChanged();
                    }
                    if(dbQueries.cartItemModelList.size()==0){
                        cartItemRecyclerView.setVisibility(View.INVISIBLE);
                        emptyCart.setVisibility(View.VISIBLE);
                        cartTotalAmtLayout.setVisibility(View.INVISIBLE);
                    }
                    else if(dbQueries.cartItemModelList.size()!=0) {
                        cartItemRecyclerView.setVisibility(View.VISIBLE);
                        emptyCart.setVisibility(View.INVISIBLE);
                        cartTotalAmtLayout.setVisibility(View.VISIBLE);
                        cartItemRecyclerView.setAdapter(MainActivity.cartAdapter);
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Could not load cart",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CartList> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

//                        else{
//                            final String serviceType=task.getResult().get("service_id_" + i + "_type").toString();
//                            final int finalI2 = i;
//                            fstore.collection(serviceType).document(task.getResult().get("service_id_" + i).toString())
//                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                                @Override
//                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                    DocumentSnapshot documentSnapshot = task.getResult();
//                                    if (documentSnapshot.exists()) {
//                                        dbQueries.cartItemModelList.add(new CartItemModel(documentSnapshot.get("icon").toString(),
//                                                documentSnapshot.getId(),
//                                                documentSnapshot.get("price").toString(), serviceType,
//                                                documentSnapshot.getId(), finalI2,documentSnapshot.get("Time").toString()));
//                                        MainActivity.cartAdapter.notifyDataSetChanged();
//                                    }
//                                }
//                            });
//                        }

    }
    @Override
    protected void onStart() {
        super.onStart();
        //dbQueries.loadCartList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
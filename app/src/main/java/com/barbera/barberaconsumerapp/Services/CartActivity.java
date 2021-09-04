 package com.barbera.barberaconsumerapp.Services;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.barbera.barberaconsumerapp.HomeActivity;
import com.barbera.barberaconsumerapp.R;
import com.barbera.barberaconsumerapp.Utils.CartItemModel;
import com.barbera.barberaconsumerapp.Utils.CartList;
import com.barbera.barberaconsumerapp.dbQueries;
import com.barbera.barberaconsumerapp.network_aws.JsonPlaceHolderApi2;
import com.barbera.barberaconsumerapp.network_aws.RetrofitClientInstanceCart;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CartActivity extends AppCompatActivity {
    public static RecyclerView cartItemRecyclerView;
    public static ProgressBar progressBarMyCart;
    public static TextView total_cart_amount;
    public static TextView total_cart_quantity;
    public static int totalAmount=0,quantity=0;
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
        total_cart_quantity=findViewById(R.id.Total_cart_item);

        updateCartItemModelList();

        addInEmptyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, HomeActivity.class));
                finish();
            }
        });
    }

    public void updateCartItemModelList(){
        Retrofit retrofit = RetrofitClientInstanceCart.getRetrofitInstance();
        JsonPlaceHolderApi2 jsonPlaceHolderApi2 = retrofit.create(JsonPlaceHolderApi2.class);
        SharedPreferences preferences = getSharedPreferences("Token", MODE_PRIVATE);
        String token = preferences.getString("token", "no");
        CartActivity.progressBarMyCart.setVisibility(View.VISIBLE);
        dbQueries.cartItemModelList.clear();
        Call<CartList> call= jsonPlaceHolderApi2.getCart("Bearer "+token);
        call.enqueue(new Callback<CartList>() {
            @Override
            public void onResponse(Call<CartList> call, Response<CartList> response) {
                if(response.code()==200){
                    CartList cartList=response.body();
                    int count=cartList.getCount();
                    totalAmount=0;
                    quantity=0;
                    if(count!=0){
                        List<CartItemModel> list=cartList.getList();
                        for(CartItemModel itemModel:list) {
                            //Log.d("yo",itemModel.getType());
                            dbQueries.cartItemModelList.add(new CartItemModel(null,itemModel.getServiceName(),itemModel.getServicePrice(),
                                    itemModel.getCategory(),itemModel.getQuantity(),itemModel.getTime(),itemModel.getId(),false,itemModel.getType()));
                            totalAmount+=(itemModel.getQuantity()*itemModel.getServicePrice());
                            quantity+=itemModel.getQuantity();
                        }
                        total_cart_amount.setText("Rs "+totalAmount);
                        total_cart_quantity.setText("(For "+quantity+" items)");

                        //HomeActivity.cartAdapter.notifyDataSetChanged();
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
                        cartItemRecyclerView.setAdapter(HomeActivity.cartAdapter);
                    }
                    CartActivity.progressBarMyCart.setVisibility(View.GONE);
                }
                else{
                    CartActivity.progressBarMyCart.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Could not load cart",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CartList> call, Throwable t) {
                CartActivity.progressBarMyCart.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        //dbQueries.loadCartList();
    }

}
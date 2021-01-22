package com.barbera.barberaconsumerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CartActivity extends AppCompatActivity {
    public static RecyclerView cartItemRecyclerView;
    private DocumentReference documentReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore fstore;
    public static ProgressBar progressBarMyCart;
    public static TextView total_cart_amount;
    public static int totalAmount=0;
    public static Button continueToBooking;
    public static RelativeLayout emptyCart;
    public static RelativeLayout cartTotalAmtLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartItemRecyclerView=(RecyclerView) findViewById(R.id.cart_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        cartItemRecyclerView.setLayoutManager(layoutManager);
        firebaseAuth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        progressBarMyCart=(ProgressBar)findViewById(R.id.progressBarOnMyCart);
        total_cart_amount=(TextView)findViewById(R.id.total_cart_amount);
        continueToBooking=(Button)findViewById(R.id.continue_from_cart_button);
        emptyCart=(RelativeLayout)findViewById(R.id.empty_cart);
        Button addInEmptyCart=(Button)findViewById(R.id.add_a_service);
        cartTotalAmtLayout=(RelativeLayout)findViewById(R.id.cart_total_amount_layout);

        addInEmptyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this,MainActivity.class));
                finish();
            }
        });



        documentReference=fstore.collection("Users").document(firebaseAuth.getCurrentUser().getUid())
                .collection("UserData").document("MyCart");

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

    public static void updateCartItemModelList(){
        //CartActivity.progressBarMyCart.setVisibility(View.VISIBLE);
        dbQueries.cartItemModelList.clear();
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        final FirebaseFirestore fstore=FirebaseFirestore.getInstance();
        fstore.collection("Users").document(firebaseAuth.getCurrentUser().getUid())
                .collection("UserData").document("MyCart").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    for (int i = 1; i <= (long) task.getResult().get("cart_list_size"); i++) {
                        if (task.getResult().get("service_id_" + i + "_type").toString().equals("men")) {
                            final int finalI = i;
                            fstore.collection("Men\'s Salon").document(task.getResult().get("service_id_" + i).toString()).get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            DocumentSnapshot documentSnapshot = task.getResult();
                                            if(documentSnapshot.exists()) {
                                                dbQueries.cartItemModelList.add(new CartItemModel(documentSnapshot.get("icon").toString(),
                                                        documentSnapshot.get("Service_title").toString(),
                                                        documentSnapshot.get("price").toString(), documentSnapshot.get("type").toString(),
                                                        documentSnapshot.getId(), finalI));
                                                MainActivity.cartAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    });
                        }
                        else if (task.getResult().get("service_id_" + i + "_type").toString().equals("women")) {
                            final int finalI1 = i;
                            fstore.collection("Women\'s Salon").document(task.getResult().get("service_id_" + i).toString()).get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            DocumentSnapshot documentSnapshot = task.getResult();
                                            if (documentSnapshot.exists()) {
                                                dbQueries.cartItemModelList.add(new CartItemModel
                                                        (documentSnapshot.get("icon").toString(),
                                                                documentSnapshot.get("Service_title").toString(),
                                                                documentSnapshot.get("price").toString(), documentSnapshot.get("type").toString(),
                                                                documentSnapshot.getId(), finalI1));
                                                MainActivity.cartAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    });
                        }
                        else{
                            final String serviceType=task.getResult().get("service_id_" + i + "_type").toString();
                            final int finalI2 = i;
                            fstore.collection(serviceType).document(task.getResult().get("service_id_" + i).toString())
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if (documentSnapshot.exists()) {
                                        dbQueries.cartItemModelList.add(new CartItemModel
                                                (null,
                                                        documentSnapshot.getId(),
                                                        documentSnapshot.get("Price").toString(), serviceType,
                                                        documentSnapshot.getId(), finalI2));
                                        MainActivity.cartAdapter.notifyDataSetChanged();
                                    }
                                }
                            });
                        }
                    }
                    //cartItemRecyclerView.setAdapter(MainActivity.cartAdapter);
                    //CartActivity.progressBarMyCart.setVisibility(View.INVISIBLE);
                   // MainActivity.cartAdapter.notifyDataSetChanged();
                }
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        dbQueries.loadCartList();
    }
}
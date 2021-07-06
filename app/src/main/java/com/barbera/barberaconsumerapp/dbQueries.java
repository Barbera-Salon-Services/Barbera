package com.barbera.barberaconsumerapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.barbera.barberaconsumerapp.network_aws.JsonPlaceHolderApi2;
import com.barbera.barberaconsumerapp.network_aws.RetrofitClientInstance2;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

public class dbQueries {
    public static List<String> cartList=new ArrayList<>();
    public static List<CartItemModel> cartItemModelList=new ArrayList<>();
    public static List<SlideModel> slideModelList=new ArrayList<>();
    private static String token;

    public static void loadCartList(){
        cartList.clear();
//        Retrofit retrofit = RetrofitClientInstance2.getRetrofitInstance();
//        JsonPlaceHolderApi2 jsonPlaceHolderApi2 = retrofit.create(JsonPlaceHolderApi2.class);
//        SharedPreferences preferences = getSharedPreferences("Token", MODE_PRIVATE);
//        token = preferences.getString("token", "no");
        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("UserData").document("MyCart").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot=task.getResult();
                            for(long i = 1; i<=(long) (documentSnapshot.get("cart_list_size")); i++) {
                                cartList.add(documentSnapshot.get("service_id_" + i).toString());
                            }
                        }
                    }
                });
    }

    public static void loadslideModelList() {

        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("AppData").document("Sliders").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    for(long i=1;i<=(long)task.getResult().get("Number_of_sliders");i++)
                        slideModelList.add(new SlideModel((String) task.getResult().get("slider_"+i)));
                }
            }
        });
    }

}

package com.barbera.barberaconsumerapp;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

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

public class dbQueries {
    public static List<String> cartList=new ArrayList<>();
    public static List<CartItemModel> cartItemModelList=new ArrayList<>();
    public static List<SlideModel> slideModelList=new ArrayList<>();


    public static void loadCartList(){
        cartList.clear();
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

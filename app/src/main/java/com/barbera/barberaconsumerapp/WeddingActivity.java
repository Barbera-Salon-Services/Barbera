package com.barbera.barberaconsumerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.barbera.barberaconsumerapp.R.drawable.bride_background;

public class WeddingActivity extends AppCompatActivity {
    private TextView weddingHeading;
    private String weddingType;
    private RecyclerView weddingRecyclerView;
    private static List<WeddingModel> brideList=new ArrayList<>();
    private static List<WeddingModel> groomList=new ArrayList<>();
    private ProgressBar progressBarwedding;
    private LinearLayoutManager bridelayoutmanager,groomlayoutmanager;
    private WeddingAdapter brideAdapter,groomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wedding);

        Intent intent=getIntent();
        weddingType=intent.getStringExtra("Type");
        weddingHeading=(TextView)findViewById(R.id.wedding_section_heading);
        weddingRecyclerView=(RecyclerView)findViewById(R.id.wedding_recycler_view);
        progressBarwedding=(ProgressBar)findViewById(R.id.progress_bar_on_wedding_section);
        bridelayoutmanager=new LinearLayoutManager(getApplicationContext());
        bridelayoutmanager.setOrientation(RecyclerView.HORIZONTAL);
        groomlayoutmanager=new LinearLayoutManager(getApplicationContext());
        groomlayoutmanager.setOrientation(RecyclerView.HORIZONTAL);
        brideAdapter=new WeddingAdapter(brideList);
        groomAdapter=new WeddingAdapter(groomList);

        RelativeLayout weddingActLayout=(RelativeLayout)findViewById(R.id.weddingLayout);

        weddingHeading.setText(weddingType);

        if(weddingType.equals("Bride"))
            weddingActLayout.setBackgroundResource(bride_background);
        else if(weddingType.equals("Groom"))
            weddingActLayout.setBackgroundResource(R.drawable.groom_background);


        if(weddingType.equals("Bride")&&brideList.size()==0)
            loadbridalList();
        else if(weddingType.equals("Groom")&&groomList.size()==0)
            loadgroomList();
//        else if(weddingType.equals("Mehndi")&&mehndiList.size()==0)
//            loadMehndiList();
//        else if(weddingType.equals("Makeup")&&makeupList.size()==0)
//            loadMakeupList();
        else if(weddingType.equals("Bride")&&brideList.size()!=0) {
            weddingRecyclerView.setLayoutManager(bridelayoutmanager);
            weddingRecyclerView.setAdapter(brideAdapter);
        }
        else if(weddingType.equals("Groom")&&groomList.size()!=0) {
            weddingRecyclerView.setLayoutManager(groomlayoutmanager);
            weddingRecyclerView.setAdapter(groomAdapter);
        }
//        else if(weddingType.equals("Mehndi")&&mehndiList.size()!=0) {
//            weddingRecyclerView.setLayoutManager(mehndilayoutmanager);
//            weddingRecyclerView.setAdapter(mehndiAdapter);
//        }
//        else if(weddingType.equals("Makeup")&&makeupList.size()!=0) {
//            weddingRecyclerView.setLayoutManager(makeuplayoutmanager);
//            weddingRecyclerView.setAdapter(makeupAdapter);
//        }

    }

    private void loadbridalList() {
        progressBarwedding.setVisibility(View.VISIBLE);
        FirebaseFirestore.getInstance().collection("BridalPackages").orderBy("price").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
                                brideList.add(new WeddingModel(documentSnapshot.getId(),documentSnapshot.get("Services").toString(),
                                        documentSnapshot.get("price").toString(),"BridalPackages",null,null));
                            }
                            weddingRecyclerView.setLayoutManager(bridelayoutmanager);
                            weddingRecyclerView.setAdapter(brideAdapter);
                            progressBarwedding.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    private void loadgroomList(){
        progressBarwedding.setVisibility(View.VISIBLE);
        FirebaseFirestore.getInstance().collection("GroomPackages").orderBy("price").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
                                groomList.add(new WeddingModel(documentSnapshot.getId(),documentSnapshot.get("Services").toString(),
                                        documentSnapshot.get("price").toString(),"GroomPackages",null,null));
                            }
                            weddingRecyclerView.setLayoutManager(groomlayoutmanager);
                            weddingRecyclerView.setAdapter(groomAdapter);
                            progressBarwedding.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }
//    private void loadMehndiList(){
//        progressBarwedding.setVisibility(View.VISIBLE);
//        FirebaseFirestore.getInstance().collection("Mehandi").orderBy("price").get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if(task.isSuccessful()){
//                            for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
//                                mehndiList.add(new WeddingModel(documentSnapshot.getId(),null,documentSnapshot.get("price").toString(),
//                                        "Mehandi",documentSnapshot.get("cut_price").toString(),documentSnapshot.get("icon").toString()));
//                            }
//                            weddingRecyclerView.setLayoutManager(mehndilayoutmanager);
//                            weddingRecyclerView.setAdapter(mehndiAdapter);
//                            progressBarwedding.setVisibility(View.INVISIBLE);
//                        }
//                    }
//                });
//    }
//    private void loadMakeupList(){
//        progressBarwedding.setVisibility(View.VISIBLE);
//        FirebaseFirestore.getInstance().collection("WeddingMakeup").orderBy("price").get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if(task.isSuccessful()){
//                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
//                                makeupList.add(new WeddingModel(documentSnapshot.getId(),null,documentSnapshot.get("price").toString(),
//                                        "WeddingMakeup",documentSnapshot.get("cut_price").toString(),documentSnapshot.get("icon").toString()));
//                            }
//                            weddingRecyclerView.setLayoutManager(makeuplayoutmanager);
//                            weddingRecyclerView.setAdapter(makeupAdapter);
//                            progressBarwedding.setVisibility(View.INVISIBLE);
//                        }
//                    }
//                });
//    }
}
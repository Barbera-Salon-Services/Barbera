package com.barbera.barberaconsumerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

//import static com.barbera.barberaconsumerapp.R.drawable.bride_background;

public class WeddingActivity extends AppCompatActivity {
    private ImageView weddingHeading;
    private String weddingType;
    private RecyclerView weddingRecyclerView;
    private static List<WeddingModel> brideList=new ArrayList<>();
    private static List<WeddingModel> groomList=new ArrayList<>();
    private ProgressBar progressBarwedding;
    private LinearLayoutManager bridelayoutmanager;
    private LinearLayoutManager groomlayoutmanager;
    private WeddingAdapter brideAdapter;
    private WeddingAdapter groomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wedding);

        Intent intent=getIntent();
        weddingType=intent.getStringExtra("Type");
        weddingHeading=(ImageView) findViewById(R.id.heading_view);
        weddingRecyclerView=(RecyclerView) findViewById(R.id.wedding_recycler_view);
        progressBarwedding=(ProgressBar)findViewById(R.id.progress_bar_on_wedding_section);
        bridelayoutmanager=new LinearLayoutManager(getApplicationContext());
        bridelayoutmanager.setOrientation(RecyclerView.HORIZONTAL);
        groomlayoutmanager=new LinearLayoutManager(getApplicationContext());
        groomlayoutmanager.setOrientation(RecyclerView.HORIZONTAL);
        brideAdapter=new WeddingAdapter(brideList);
        groomAdapter=new WeddingAdapter(groomList);
        RelativeLayout weddingActLayout=(RelativeLayout)findViewById(R.id.weddingLayout);

        //PagerSnapHelper pagerSnapHelper;
        new PagerSnapHelper().attachToRecyclerView(weddingRecyclerView);

        //weddingHeading.setText(weddingType);
        FirebaseFirestore.getInstance().collection("AppData").document("Wedding").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            if(weddingType.equals("Bride"))
                                Glide.with(getApplicationContext()).load(task.getResult().get("bridal").toString())
                                    .apply(new RequestOptions().placeholder(R.drawable.logo)).into(weddingHeading);
                            else
                                Glide.with(getApplicationContext()).load(task.getResult().get("groom").toString())
                                        .apply(new RequestOptions().placeholder(R.drawable.logo)).into(weddingHeading);

                        }
                    }
                });

      /*  if(weddingType.equals("Bride"))
            weddingHeading.setBackgroundResource(R.drawable.bridal_heading);
        else
            weddingHeading.setBackgroundResource(R.drawable.groom_heading);*/


        if(weddingType.equals("Bride")&&brideList.size()==0)
            loadbridalList();
        else if(weddingType.equals("Groom")&&groomList.size()==0)
            loadgroomList();
        else if(weddingType.equals("Bride")&&brideList.size()!=0) {
            weddingRecyclerView.setLayoutManager(bridelayoutmanager);
            weddingRecyclerView.setAdapter(brideAdapter);
        }
        else if(weddingType.equals("Groom")&&groomList.size()!=0) {
            weddingRecyclerView.setLayoutManager(groomlayoutmanager);
            weddingRecyclerView.setAdapter(groomAdapter);
        }

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
                                        documentSnapshot.get("price").toString(),"BridalPackages",(List<String>)documentSnapshot.get("sittings")));
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
                                        documentSnapshot.get("price").toString(),"GroomPackages",(List<String>)documentSnapshot.get("sittings")));
                            }
                            weddingRecyclerView.setLayoutManager(groomlayoutmanager);
                            weddingRecyclerView.setAdapter(groomAdapter);
                            progressBarwedding.setVisibility(View.INVISIBLE);
                        }
                    }
                });

    }
}
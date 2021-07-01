package com.barbera.barberaconsumerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class WeddingPreActivity extends AppCompatActivity {
    private CardView bridal;
    private CardView groom;
    private CardView mehandi;
    private CardView makeup;
    private Intent intent;
    private Intent intent1;
    private String bridalImage;
    private String groomImage;
    private String mehandiImage;
    private String makeupImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wedding_pre);
        //test comment
        bridal=(CardView)findViewById(R.id.bridal_section);
        groom=(CardView)findViewById(R.id.groom_section);
        mehandi=(CardView)findViewById(R.id.mehandi_section);
        makeup=(CardView)findViewById(R.id.makeup_section);
        intent=new Intent(getApplicationContext(),WeddingActivity.class);
        final Intent intent1 =new Intent(getApplicationContext(),MehendiMakeupActivity.class);

        FirebaseFirestore.getInstance().collection("AppData").document("Wedding").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            bridalImage=task.getResult().get("bridal").toString();
                            groomImage=task.getResult().get("groom").toString();
                            mehandiImage=task.getResult().get("mehandi").toString();
                            makeupImage=task.getResult().get("makeup").toString();
                        }
                    }
                });
        //Glide.with(getApplicationContext()).load(bridalImage)
               // .apply(new RequestOptions().placeholder(R.drawable.logo)).into(bridal);

        bridal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("Type","Bride");
                startActivity(intent);
            }
        });

        groom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("Type","Groom");
                startActivity(intent);
            }
        });

        mehandi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent1.putExtra("SalonType","Mehandi");
                intent1.putExtra("Category", "Mehandi");
                intent1.putExtra("SubCategDoc","Null");
                //intent1.putExtra("ServiceType","Mehandi");
                startActivity(intent1);
            }
        });
        makeup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent1.putExtra("SalonType","WeddingMakeup");
                intent1.putExtra("Category", "WeddingMakeup");
                intent1.putExtra("SubCategDoc","Null");
                //intent1.putExtra("ServiceType","WeddingMakeup");
                startActivity(intent1);
            }
        });
    }
}
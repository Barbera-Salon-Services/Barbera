package com.barbera.barberaconsumerapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.barbera.barberaconsumerapp.Utils.CheckedModel;
import com.barbera.barberaconsumerapp.Utils.MehendiAdapter;
import com.barbera.barberaconsumerapp.network_aws.JsonPlaceHolderApi2;
import com.barbera.barberaconsumerapp.network_aws.RetrofitClientInstanceUser;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Retrofit;

public class MehendiMakeupActivity extends AppCompatActivity {
    private FirebaseFirestore fstore;
    private FirebaseAuth firebaseAuth;
    private ListView listView;
    public ProgressBar progressBarOnServiceList;
    public List<Service> serviceList=new ArrayList<Service>();
    // public List<Service> womenserviceList=new ArrayList<Service>();
    private String Category;
    private String CategoryIMage="";
    private ImageView image;
    public static String salontype;
    public static List<CheckedModel> checkeditemList=new ArrayList<>();
    public Button addToCart;
    public static Button bookNow;
    private String collecton;
    private String subCategoryDocument;
    private String serViceType,token;
    private static TextView numberCartParlour;
    private List<String> subCategoryList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mehmakeup);

        Intent intent=getIntent();
        Category=intent.getStringExtra("Category");
        CategoryIMage=intent.getStringExtra("CategoryIMage");
        salontype=intent.getStringExtra("SalonType");
        collecton=intent.getStringExtra("Collection");
        subCategoryDocument=intent.getStringExtra("SubCategDoc");
        serViceType=intent.getStringExtra("ServiceType");
        fstore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        listView=(ListView) findViewById(R.id.new_service_recycler_view);
        TextView title=(TextView)findViewById(R.id.new_service_title);
        progressBarOnServiceList=(ProgressBar)findViewById(R.id.new_service_progress_bar);
        ImageView cart=(ImageView)findViewById(R.id.new_service_cart);
        final MehendiAdapter adapter = new MehendiAdapter(getApplicationContext(),serviceList);
        image=(ImageView)findViewById(R.id.new_service_image);
        addToCart=(Button)findViewById(R.id.new_service_add_to_cart);
        bookNow=(Button)findViewById(R.id.new_service_book_now_button);
        numberCartParlour=(TextView)findViewById(R.id.numberOnCartParlour);
        subCategoryList=new ArrayList<>();

        Retrofit retrofit = RetrofitClientInstanceUser.getRetrofitInstance();
        JsonPlaceHolderApi2 jsonPlaceHolderApi2 = retrofit.create(JsonPlaceHolderApi2.class);
        SharedPreferences preferences = getSharedPreferences("Token", MODE_PRIVATE);
        token = preferences.getString("token", "no");

        //listView.setNumColumns(1);

        FirebaseMessaging.getInstance().subscribeToTopic("men")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Successful";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }
                    }
                });
        addToCart.setOnClickListener(v -> {
            Intent intent1 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "+916377894199"));
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent1);
        });
//        cart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
//                    Toast.makeText(getApplicationContext(), "You Must Log In to continue", Toast.LENGTH_LONG).show();
//                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                } else {
//                    startActivity(new Intent(MehendiMakeupActivity.this, CartActivity.class));
//                }
//            }
//        });
        if(!subCategoryDocument.equals("Null"))
            title.setText(subCategoryDocument);
        else
            title.setText(Category);

        if(salontype.equals("men")||salontype.equals("women")) {
            if (subCategoryDocument.equals("Null")) {
                progressBarOnServiceList.setVisibility(View.VISIBLE);
                Glide.with(getApplicationContext()).load(CategoryIMage)
                        .apply(new RequestOptions().placeholder(R.drawable.logo)).into(image);
                fstore.collection(collecton).document(Category).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (int i = 1; i <= (long) task.getResult().get("Services"); i++) {
                                        fstore.collection(serViceType).document(task.getResult().get("Service_" + i).toString()).get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        DocumentSnapshot documentSnapshot = task.getResult();
                                                        if (documentSnapshot.exists()) {
                                                            serviceList.add(new Service(documentSnapshot.get("icon").toString().trim(),
                                                                    documentSnapshot.get("Service_title").toString(),
                                                                    documentSnapshot.get("price").toString(), documentSnapshot.getId(),
                                                                    documentSnapshot.get("type").toString(),
                                                                    documentSnapshot.get("cut_price").toString(), documentSnapshot.get("Time").toString(),documentSnapshot.get("details").toString()));
                                                            listView.setAdapter(adapter);
                                                            progressBarOnServiceList.setVisibility(View.INVISIBLE);
                                                        }
                                                    }
                                                });
                                    }
                                }
                            }
                        });
            }
            else {
                progressBarOnServiceList.setVisibility(View.VISIBLE);
                fstore.collection(collecton).document(Category).collection("SubCategories").document(subCategoryDocument).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    subCategoryList=(List<String>)task.getResult().get("Services");
                                    Glide.with(getApplicationContext()).load(task.getResult().get("icon").toString())
                                            .apply(new RequestOptions().placeholder(R.drawable.logo)).into(image);
                                    for(int i=0;i<(int)subCategoryList.size();i++){
                                        fstore.collection(serViceType).document(subCategoryList.get(i)).get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        DocumentSnapshot documentSnapshot=task.getResult();
                                                        if(documentSnapshot.exists()){
                                                            serviceList.add(new Service(Objects.requireNonNull(documentSnapshot.get("icon")).toString(),
                                                                    documentSnapshot.get("Service_title").toString(),
                                                                    documentSnapshot.get("price").toString(), documentSnapshot.getId(), documentSnapshot.get("type").toString(),
                                                                    documentSnapshot.get("cut_price").toString(),
                                                                    documentSnapshot.get("Time") != null ? documentSnapshot.get("Time").toString() : null
                                                                    ,documentSnapshot.get("details").toString()));
                                                            listView.setAdapter(adapter);
                                                            progressBarOnServiceList.setVisibility(View.INVISIBLE);

                                                        }
                                                    }
                                                });
                                    }
                                }
                            }
                        });
            }
        }
        else{
            progressBarOnServiceList.setVisibility(View.VISIBLE);
            fstore.collection("AppData").document("Wedding").get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                if(salontype.equals("Mehandi"))
                                    Glide.with(getApplicationContext()).load(task.getResult().get("mehandi").toString())
                                            .apply(new RequestOptions().placeholder(R.drawable.logo)).into(image);
                                else
                                    Glide.with(getApplicationContext()).load(task.getResult().get("makeup").toString())
                                            .apply(new RequestOptions().placeholder(R.drawable.logo)).into(image);
                            }
                        }
                    });
          /*  if(salontype.equals("Mehandi"))
                image.setImageResource(R.drawable.wedding_mehandi);
            else
                image.setImageResource(R.drawable.make_up_wedding);*/

            fstore.collection(salontype).orderBy("price").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
                            serviceList.add(new Service(documentSnapshot.get("icon").toString(),documentSnapshot.getId(),documentSnapshot.get("price").toString(),
                                    documentSnapshot.getId(),salontype,documentSnapshot.get("cut_price").toString(),null,documentSnapshot.get("details").toString()));
                        }
                        listView.setAdapter(adapter);
                        progressBarOnServiceList.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }

       /* else if(salontype.equals("women")){
           progressBarOnServiceList.setVisibility(View.VISIBLE);
           fstore.collection("WomenCategory").document(Category).get()
                   .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                       @Override
                       public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                           if(task.isSuccessful()){
                               for(int i=1;i<=(long)task.getResult().get("Services");i++){
                                   fstore.collection("Women\'s Salon").document(task.getResult().get("Service_"+i).toString()).get()
                                           .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                               @Override
                                               public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                   DocumentSnapshot documentSnapshot = task.getResult();
                                                   if(documentSnapshot.exists()){
                                                       womenserviceList.add(new Service(documentSnapshot.get("icon").toString(), documentSnapshot.get("Service_title").toString(),
                                                               documentSnapshot.get("price").toString(), documentSnapshot.getId(), documentSnapshot.get("type").toString(),
                                                               documentSnapshot.get("cut_price").toString(),documentSnapshot.get("Time").toString()));
                                                       listView.setAdapter(womenadapter);
                                                       progressBarOnServiceList.setVisibility(View.INVISIBLE);
                                                   }
                                               }
                                           });
                               }
                           }
                       }
                   });
       }*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        checkeditemList.clear();
    }
    public void loadNumberOnCartParlour(){
        if(token.equals("no"))
            numberCartParlour.setText("0");
        else {
            SharedPreferences sharedPreferences=getSharedPreferences("Count",MODE_PRIVATE);
            int count=sharedPreferences.getInt("count",0);
            numberCartParlour.setText(""+count);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadNumberOnCartParlour();
    }
}

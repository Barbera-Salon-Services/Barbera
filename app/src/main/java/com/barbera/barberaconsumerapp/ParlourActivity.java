package com.barbera.barberaconsumerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.barbera.barberaconsumerapp.Utils.CheckedModel;
import com.barbera.barberaconsumerapp.Utils.ServiceItem;
import com.barbera.barberaconsumerapp.Utils.ServiceList;
import com.barbera.barberaconsumerapp.network_aws.JsonPlaceHolderApi2;
import com.barbera.barberaconsumerapp.network_aws.RetrofitClientInstanceService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ParlourActivity extends AppCompatActivity {
    private FirebaseFirestore fstore;
    private FirebaseAuth firebaseAuth;
    private ListView listView;
    public static ProgressBar progressBarOnServiceList;
    public List<ServiceItem> serviceList=new ArrayList<ServiceItem>();
   // public List<Service> womenserviceList=new ArrayList<Service>();
    private String Category;
    private String CategoryIMage="";
    private ImageView image;
    public static String salontype;
    public static List<CheckedModel> checkeditemList=new ArrayList<>();
    public static Button addToCart;
    public static Button bookNow;
    private String collecton,token;
    private String subCategoryDocument;
    private String serViceType;
    private static TextView numberCartParlour;
    private List<String> subCategoryList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_service_activity);

        Intent intent = getIntent();
        Category = intent.getStringExtra("Category");
        CategoryIMage = intent.getStringExtra("CategoryIMage");
        salontype = intent.getStringExtra("SalonType");
        collecton = intent.getStringExtra("Collection");
        subCategoryDocument = intent.getStringExtra("SubCategDoc");
        serViceType = intent.getStringExtra("ServiceType");
        fstore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        listView = (ListView) findViewById(R.id.new_service_recycler_view);
        TextView title = (TextView) findViewById(R.id.new_service_title);
        ParlourActivity.progressBarOnServiceList = (ProgressBar) findViewById(R.id.new_service_progress_bar);
        ImageView cart = (ImageView) findViewById(R.id.new_service_cart);
        final ServiceAdapter adapter = new ServiceAdapter(getApplicationContext(), serviceList);
        //final ServiceAdapter womenadapter = new ServiceAdapter(womenserviceList);
        image = (ImageView) findViewById(R.id.new_service_image);
        addToCart = (Button) findViewById(R.id.new_service_add_to_cart);
        bookNow = (Button) findViewById(R.id.new_service_book_now_button);
        numberCartParlour = (TextView) findViewById(R.id.numberOnCartParlour);
        subCategoryList = new ArrayList<>();

        //listView.setNumColumns(1);
        Retrofit retrofit = RetrofitClientInstanceService.getRetrofitInstance();
        JsonPlaceHolderApi2 jsonPlaceHolderApi2 = retrofit.create(JsonPlaceHolderApi2.class);
        SharedPreferences preferences = getSharedPreferences("Token", MODE_PRIVATE);
        token = preferences.getString("token", "no");

//        FirebaseMessaging.getInstance().subscribeToTopic("men")
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        String msg = "Successful";
//                        if (!task.isSuccessful()) {
//                            msg = "Failed";
//                        }
//                    }
//                });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (token.equals("no")) {
                    Toast.makeText(getApplicationContext(), "You Must Log In to continue", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), ActivityPhoneVerification.class));
                } else {
                    startActivity(new Intent(ParlourActivity.this, CartActivity.class));
                }
            }
        });
        if (!subCategoryDocument.equals("Null"))
            title.setText(subCategoryDocument);
        else
            title.setText(Category);

        if (salontype.equals("male") || salontype.equals("female")) {
            progressBarOnServiceList.setVisibility(View.VISIBLE);
            Glide.with(getApplicationContext()).load(CategoryIMage)
                    .apply(new RequestOptions().placeholder(R.drawable.logo)).into(image);
            Call<ServiceList> call = jsonPlaceHolderApi2.getAllServices(salontype, new ServiceItem(null, 0, 0, null, 0, null, Category,
                    false, null, false, null), "Bearer "+token);
            call.enqueue(new Callback<ServiceList>() {
                @Override
                public void onResponse(Call<ServiceList> call, Response<ServiceList> response) {
                    if (response.code() == 200) {
                        ServiceList serviceList1 = response.body();
                        List<ServiceItem> list = serviceList1.getServices();
                        for (ServiceItem item : list) {
                            serviceList.add(new ServiceItem(item.getName(), item.getPrice(), item.getTime(), item.getDetail(),
                                    item.getCutprice(), item.getGender(), item.getType(), item.isDod(), item.getId(), item.isTrend(), item.getSubtype()));
                        }
                        listView.setAdapter(adapter);
                        progressBarOnServiceList.setVisibility(View.INVISIBLE);
                    } else {
                        Toast.makeText(getApplicationContext(), "Could not load services", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ServiceList> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            progressBarOnServiceList.setVisibility(View.VISIBLE);
            fstore.collection("AppData").document("Wedding").get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                if (salontype.equals("Mehandi"))
                                    Glide.with(getApplicationContext()).load(task.getResult().get("mehandi").toString())
                                            .apply(new RequestOptions().placeholder(R.drawable.logo)).into(image);
                                else
                                    Glide.with(getApplicationContext()).load(task.getResult().get("makeup").toString())
                                            .apply(new RequestOptions().placeholder(R.drawable.logo)).into(image);
                            }
                        }
                    });
        }

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

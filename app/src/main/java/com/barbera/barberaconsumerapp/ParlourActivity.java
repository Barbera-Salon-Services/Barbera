package com.barbera.barberaconsumerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class ParlourActivity extends AppCompatActivity {
    private FirebaseFirestore fstore;
    private FirebaseAuth firebaseAuth;
    private ListView listView;
    public static ProgressBar progressBarOnServiceList;
    public List<Service> serviceList=new ArrayList<Service>();
   // public List<Service> womenserviceList=new ArrayList<Service>();
    private String Category;
    private String CategoryIMage="";
    private ImageView image;
    public static String salontype;
    public static List<CheckedModel> checkeditemList=new ArrayList<>();
    public static Button addToCart;
    public static Button bookNow;
    private String collecton;
    private String subCategoryDocument;
    private String serViceType;
    private static TextView numberCartParlour;
    private List<String> subCategoryList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_service_activity);

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
        ParlourActivity.progressBarOnServiceList=(ProgressBar)findViewById(R.id.new_service_progress_bar);
        ImageView cart=(ImageView)findViewById(R.id.new_service_cart);
        final ServiceAdapter adapter = new ServiceAdapter(getApplicationContext(),serviceList);
        //final ServiceAdapter womenadapter = new ServiceAdapter(womenserviceList);
        image=(ImageView)findViewById(R.id.new_service_image);
        addToCart=(Button)findViewById(R.id.new_service_add_to_cart);
        bookNow=(Button)findViewById(R.id.new_service_book_now_button);
        numberCartParlour=(TextView)findViewById(R.id.numberOnCartParlour);
        subCategoryList=new ArrayList<>();

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

                cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                            Toast.makeText(getApplicationContext(), "You Must Log In to continue", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), ActivityPhoneVerification.class));
                        } else {
                            startActivity(new Intent(ParlourActivity.this, CartActivity.class));
                        }
                    }
                });
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
                                                                    documentSnapshot.get("cut_price").toString(),
                                                                    documentSnapshot.get("Time").toString(),
                                                                    documentSnapshot.get("details").toString())
                                                                  );
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
                                                                    documentSnapshot.get("Time") != null ? documentSnapshot.get("Time").toString() : null,
                                                                    documentSnapshot.get("details").toString())
                                                             );
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
    public static void loadNumberOnCartParlour(){
        if(FirebaseAuth.getInstance().getCurrentUser()==null)
            numberCartParlour.setText("0");
        else {
            FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("UserData").document("MyCart").get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                               // NumberOnCartMain.setText(task.getResult().get("cart_list_size").toString());
                                //numberCartCategory.setText(task.getResult().get("cart_list_size").toString());
                                 numberCartParlour.setText(task.getResult().get("cart_list_size").toString());
                            }
                        }
                    });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadNumberOnCartParlour();
    }
}
/*progressBarOnServiceList.setVisibility(View.VISIBLE);
           fstore.collection("Men\'s Salon").orderBy("index").get()
                   .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                       @Override
                       public void onComplete(@NonNull Task<QuerySnapshot> task) {
                           if (task.isSuccessful()) {
                               for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                   menserviceList.add(new Service(documentSnapshot.get("icon").toString(), documentSnapshot.get("Service_title").toString(),
                                           documentSnapshot.get("price").toString(), documentSnapshot.getId(), documentSnapshot.get("type").toString(),
                                           documentSnapshot.get("cut_price").toString()));
                               }
                               ServiceAdapter adapter = new ServiceAdapter(menserviceList);
                               progressBarOnServiceList.setVisibility(View.INVISIBLE);
                               listView.setAdapter(adapter);
                               if (firebaseAuth.getCurrentUser()!=null&&dbQueries.cartList.size() == 0)
                                   dbQueries.loadCartList();
                           } else {
                               String msg = task.getException().getMessage();
                               progressBarOnServiceList.setVisibility(View.INVISIBLE);
                               Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                           }
                       }
                   });
       */


             /*   progressBarOnServiceList.setVisibility(View.VISIBLE);
                        FirebaseFirestore.getInstance().collection(collecton).document(Category).collection("SubCategories").document("Blow Dry").get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
@Override
public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        if (task.isSuccessful()){
        subCategoryList = (List<String>) task.getResult().get("Services");
        for (int i = 0; i < subCategoryList.size(); i++) {
        FirebaseFirestore.getInstance().collection(serViceType).document(subCategoryList.get(i).toString()).get()
        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
@Override
public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        DocumentSnapshot documentSnapshot = task.getResult();
        if (documentSnapshot.exists()) {
        serviceList.add(new Service(Objects.requireNonNull(documentSnapshot.get("icon")).toString(),
        documentSnapshot.get("Service_title").toString(),
        documentSnapshot.get("price").toString(), documentSnapshot.getId(), documentSnapshot.get("type").toString(),
        documentSnapshot.get("cut_price").toString(),
        documentSnapshot.get("Time") != null ? documentSnapshot.get("Time").toString() : null));
        listView.setAdapter(adapter);
        progressBarOnServiceList.setVisibility(View.INVISIBLE);
        }
        }
        });
        }
        }
        }
        });*/
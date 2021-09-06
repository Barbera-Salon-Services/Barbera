package com.barbera.barberaconsumerapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.barbera.barberaconsumerapp.Bookings.BarberDetailDialog;
import com.barbera.barberaconsumerapp.Bookings.BookingsActivity;
import com.barbera.barberaconsumerapp.Profile.ProfileActivity;
import com.barbera.barberaconsumerapp.Profile.ReferAndEarn;
import com.barbera.barberaconsumerapp.Services.CartActivity;
import com.barbera.barberaconsumerapp.Services.CartAdapter;
import com.barbera.barberaconsumerapp.Services.GridAdapter;
import com.barbera.barberaconsumerapp.Services.HomeActivityTopImageViewAdapter;
import com.barbera.barberaconsumerapp.Services.OfferDetailDialog;
import com.barbera.barberaconsumerapp.Services.ServiceType;
import com.barbera.barberaconsumerapp.Services.SliderAdapter;
import com.barbera.barberaconsumerapp.Services.WeddingPackageAdapter;
import com.barbera.barberaconsumerapp.Utils.Offer;
import com.barbera.barberaconsumerapp.Utils.OfferList;
import com.barbera.barberaconsumerapp.Utils.SliderItem;
import com.barbera.barberaconsumerapp.Utils.SliderList;
import com.barbera.barberaconsumerapp.Utils.TypeList;
import com.barbera.barberaconsumerapp.network_aws.JsonPlaceHolderApi2;
import com.barbera.barberaconsumerapp.network_aws.RetrofitClientInstanceOffers;
import com.barbera.barberaconsumerapp.network_aws.RetrofitClientInstanceService;
import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import eu.dkaratzas.android.inapp.update.Constants;
import eu.dkaratzas.android.inapp.update.InAppUpdateManager;
import eu.dkaratzas.android.inapp.update.InAppUpdateStatus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeActivity extends AppCompatActivity implements InAppUpdateManager.InAppUpdateHandler {

    private RecyclerView menRecyclerView;
    private GridAdapter gridAdapterMen, gridAdapterWomen;
    private WeddingPackageAdapter gridAdapterWed;
    private RecyclerView womenRecyclerView;
    private InAppUpdateManager inAppUpdateManager;
    private RecyclerView weddingRecyclerView;
    private List<String> imgUrlMen1 = new ArrayList<>(), imgUrlMen = new ArrayList<>(), imgNameMen = new ArrayList<>(),
            imgNameMen1 = new ArrayList<>(), imgUrlWomen = new ArrayList<>(), imgUrlWomen1 = new ArrayList<>(),
            imgNameWomen = new ArrayList<>(), imgNameWomen1 = new ArrayList<>(), imgUrlWed = new ArrayList<>(), imgNameWed = new ArrayList<>();
    private List<SlideModel> sliderItems = new ArrayList<>();
    private List<SliderItem> tabItems = new ArrayList<>();
    private String isRegistered, imagebase;
    private JsonPlaceHolderApi2 jsonPlaceHolderApi2;
    private ImageView Cart;
    private static TextView NumberOnCartMain;
    public static CartAdapter cartAdapter;
    private ImageSlider imageSlider;
    private SliderAdapter sliderAdapter;
    private HomeActivityTopImageViewAdapter tabAdapter;
    private RecyclerView tabRecyclerView;
    private CardView seeMen, seeWomen;
    private TextView seeMoreMen, seeMoreWomen;
    private Handler wheelHandler;
    private Runnable wheelRunable;
    private int a = 0, b = 0;
    private CircleImageView anim1, anim2, anim3, anim4, anim5, anim6, anim7, anim8;
    private ImageView top1, top2, top3, top4, top5;
    private ImageView men1, men2, men3, men4, men5, men6, men7;
    private TextView menText1, menText2, menText3, menText4, menText5, menText6, menText7;
    private ImageView women1, women2, women3, women4, women5, women6, women7, women8, women9;
    private TextView womenText1, womenText2, womenText3, womenText4, womenText5, womenText6, womenText7, womenText8, womenText9;
    private TextView topText1, topText2, topText3, topText4, topText5,animText;
    private LinearLayout ll1, ll2, ll3, ll4, ll5, third_women, progress_home, third_men;
    private String cat1, cat2, cat3, cat4, cat5;
    private String typ1, typ2, typ3, typ4, typ5;
    private String url1, url2, url3, url4, url5;
    private String catm = "Mens_Section", catw = "Womens_Section";
    private String typm1, typm2, typm3, typm4, typm5, typm6, typm7;
    private String urlm1, urlm2, urlm3, urlm4, urlm5, urlm6, urlm7;
    private String typw1, typw2, typw3, typw4, typw5, typw6, typw7, typw8, typw9;
    private String urlw1, urlw2, urlw3, urlw4, urlw5, urlw6, urlw7, urlw8, urlw9;
    private ScrollView scrollView;

    Animation rotateAnimation;
    CircleImageView centerImage;
    //    ImageView imageView;
    View circularView;
    List<Offer> images;
    Handler rotateHandler, imageHandler;
    Runnable rotateRunnable, imageRunnable;
    int time = 32000;
    int indexCLicked = 0;
    private static final String TAG = "Atreyee";
//    // Lucky wheel
//    private LuckyWheel lw;
//    List<WheelItem> wheelItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        seeMen = findViewById(R.id.see_more_men);
        seeWomen = findViewById(R.id.see_more_women);
        seeMoreMen = findViewById(R.id.see_men);
        seeMoreWomen = findViewById(R.id.see_women);
        third_women = findViewById(R.id.third_women);
        third_men = findViewById(R.id.third_men);
        scrollView = findViewById(R.id.scroll_view);
        progress_home = findViewById(R.id.progress_home);
        //anim = findViewById(R.id.anim_img);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
//        menRecyclerView = findViewById(R.id.men_recycler_view);
        weddingRecyclerView = findViewById(R.id.wedding_recycler_view);
//        womenRecyclerView = findViewById(R.id.women_recycler_view);
        NumberOnCartMain = (TextView) findViewById(R.id.numberOfCartMain);
        Cart = (ImageView) findViewById(R.id.cart);

        anim1 = findViewById(R.id.top);
        anim2=findViewById(R.id.northwest);
        anim3=findViewById(R.id.left);
        anim4=findViewById(R.id.southwest);
        anim5=findViewById(R.id.bottom);
        anim6=findViewById(R.id.northeast);
        anim7=findViewById(R.id.right);
        anim8=findViewById(R.id.southeast);
        animText=findViewById(R.id.anim_text);

        top1 = findViewById(R.id.different_section_images1);
        top2 = findViewById(R.id.different_section_images2);
        top3 = findViewById(R.id.different_section_images3);
        top4 = findViewById(R.id.different_section_images4);
        top5 = findViewById(R.id.different_section_images5);
        topText1 = findViewById(R.id.top_text1);
        topText2 = findViewById(R.id.top_text2);
        topText3 = findViewById(R.id.top_text3);
        topText4 = findViewById(R.id.top_text4);
        topText5 = findViewById(R.id.top_text5);
        ll1 = findViewById(R.id.ll1);
        ll2 = findViewById(R.id.ll2);
        ll3 = findViewById(R.id.ll3);
        ll4 = findViewById(R.id.ll4);
        ll5 = findViewById(R.id.ll5);
        men1 = findViewById(R.id.grid_img);
        men2 = findViewById(R.id.grid_img1);
        men3 = findViewById(R.id.grid_img2);
        men4 = findViewById(R.id.grid_img15);
        men5 = findViewById(R.id.grid_img16);
        men6 = findViewById(R.id.grid_img17);
        men7 = findViewById(R.id.grid_img3);
        women1 = findViewById(R.id.grid_img6);
        women2 = findViewById(R.id.grid_img7);
        women7 = findViewById(R.id.grid_img12);
        women3 = findViewById(R.id.grid_img8);
        women4 = findViewById(R.id.grid_img9);
        women5 = findViewById(R.id.grid_img10);
        women6 = findViewById(R.id.grid_img11);
        women8 = findViewById(R.id.grid_img13);
        women9 = findViewById(R.id.grid_img14);
        menText1 = findViewById(R.id.grid_img_text);
        menText2 = findViewById(R.id.grid_img_text1);
        menText3 = findViewById(R.id.grid_img_text2);
        menText4 = findViewById(R.id.grid_img_text15);
        menText5 = findViewById(R.id.grid_img_text16);
        menText6 = findViewById(R.id.grid_img_text17);
        menText7 = findViewById(R.id.grid_img_text3);
        womenText1 = findViewById(R.id.grid_img_text6);
        womenText2 = findViewById(R.id.grid_img_text7);
        womenText3 = findViewById(R.id.grid_img_text8);
        womenText4 = findViewById(R.id.grid_img_text9);
        womenText5 = findViewById(R.id.grid_img_text10);
        womenText6 = findViewById(R.id.grid_img_text11);
        womenText7 = findViewById(R.id.grid_img_text12);
        womenText8 = findViewById(R.id.grid_img_text13);
        womenText9 = findViewById(R.id.grid_img_text14);

        circularView = (View) findViewById(R.id.circularLayout);

//        imageView = (ImageView) findViewById(R.id.imageView);
        centerImage = (CircleImageView) findViewById(R.id.centerImage);
//        images = {R.drawable.icon1};

        // Initialise the handler
        imageHandler = new Handler();
        rotateHandler = new Handler();

        centerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OfferDetailDialog off = new OfferDetailDialog(images.get(indexCLicked).getName(),images.get(indexCLicked).getTerms(),images.get(indexCLicked).getImage(),
                        images.get(indexCLicked).getServiceId(),50,"Mens section",10/*images.get(indexCLicked).getServiceItem().getPrice(),images.get(indexCLicked).getServiceItem().getType(),images.get(indexCLicked).getServiceItem().getTime()*/);
                off.show(getSupportFragmentManager(),"true");
                off.setCancelable(true);
            }
        });


        //generateWheelItems();

//        lw = findViewById(R.id.lwv);
//        lw.addWheelItems(wheelItems);
//        lw.setTarget(1);

        cartAdapter = new CartAdapter(this);
        imageSlider = findViewById(R.id.imageSlider);
        ImageView referMain = (ImageView) findViewById(R.id.refer);
        //tabRecyclerView=findViewById(R.id.top_recycler_view);

//        tabAdapter=new HomeActivityTopImageViewAdapter(this,tabItems);
//        LinearLayoutManager tabLlm=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        tabLlm.setOrientation(RecyclerView.HORIZONTAL);
//        tabRecyclerView.setLayoutManager(tabLlm);

//        sliderAdapter=new SliderAdapter(sliderItems,HomeActivity.this);
//        LinearLayoutManager slidLlm=new LinearLayoutManager(this,RecyclerView.HORIZONTAL,true);
//        sliderRecyclerView.setLayoutManager(slidLlm);

        gridAdapterWed = new WeddingPackageAdapter(imgUrlWed, imgNameWed, this, "Wedding_Packages");
        LinearLayoutManager gridLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        weddingRecyclerView.setLayoutManager(gridLayoutManager1);

//        gridAdapterWomen= new GridAdapter(imgUrlWomen, imgNameWomen, HomeActivity.this,"Womens_Section");
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(HomeActivity.this, 3, GridLayoutManager.VERTICAL, false);
//        womenRecyclerView.setLayoutManager(gridLayoutManager);
//
//        gridAdapterMen = new GridAdapter(imgUrlMen, imgNameMen, HomeActivity.this,"Mens_Section");
//        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(HomeActivity.this, 3, GridLayoutManager.VERTICAL, false);
//        menRecyclerView.setLayoutManager(gridLayoutManager2);

        imagebase = "https://barbera-image.s3.ap-south-1.amazonaws.com/";

        SharedPreferences preferences = getSharedPreferences("Token", MODE_PRIVATE);
        isRegistered = preferences.getString("token", "no");
        Retrofit retrofit = RetrofitClientInstanceService.getRetrofitInstance();
        jsonPlaceHolderApi2 = retrofit.create(JsonPlaceHolderApi2.class);

        loadTabs();
        loadAnimation();
        loadImageSlider();
        addMenGrid();
        addWeddingGrid();
        addWomenGrid();

//        lw.setLuckyWheelReachTheTarget(new OnLuckyWheelReachTheTarget() {
//            @Override
//            public void onReachTarget() {
//                Toast.makeText(HomeActivity.this, "Target Reached", Toast.LENGTH_LONG).show();
//            }
//        });

        top1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ServiceType.class);
                intent.putExtra("SalonType", typ1);
                intent.putExtra("Category", cat1);
                intent.putExtra("ImageUrl", url1);
                startActivity(intent);
            }
        });
        top2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ServiceType.class);
                intent.putExtra("SalonType", typ2);
                intent.putExtra("Category", cat2);
                intent.putExtra("ImageUrl", url2);
                startActivity(intent);
            }
        });
        top3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ServiceType.class);
                intent.putExtra("SalonType", typ3);
                intent.putExtra("Category", cat3);
                intent.putExtra("ImageUrl", url3);
                startActivity(intent);
            }
        });
        top4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ServiceType.class);
                intent.putExtra("SalonType", typ4);
                intent.putExtra("Category", cat4);
                intent.putExtra("ImageUrl", url4);
                startActivity(intent);
            }
        });
        top5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ServiceType.class);
                intent.putExtra("SalonType", typ5);
                intent.putExtra("Category", cat5);
                intent.putExtra("ImageUrl", url5);
                startActivity(intent);
            }
        });
        men5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ServiceType.class);
                intent.putExtra("SalonType", "Mens_Section");
                intent.putExtra("Category", typm5);
                intent.putExtra("ImageUrl", urlm5);
                startActivity(intent);
            }
        });
        men1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ServiceType.class);
                intent.putExtra("SalonType", "Mens_Section");
                intent.putExtra("Category", typm1);
                intent.putExtra("ImageUrl", urlm1);
                startActivity(intent);
            }
        });
        men2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ServiceType.class);
                intent.putExtra("SalonType", "Mens_Section");
                intent.putExtra("Category", typm2);
                intent.putExtra("ImageUrl", urlm2);
                startActivity(intent);
            }
        });
        men3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ServiceType.class);
                intent.putExtra("SalonType", "Mens_Section");
                intent.putExtra("Category", typm3);
                intent.putExtra("ImageUrl", urlm3);
                startActivity(intent);
            }
        });
        men4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ServiceType.class);
                intent.putExtra("SalonType", "Mens_Section");
                intent.putExtra("Category", typm4);
                intent.putExtra("ImageUrl", urlm4);
                startActivity(intent);
            }
        });
        men6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ServiceType.class);
                intent.putExtra("SalonType", "Mens_Section");
                intent.putExtra("Category", typm6);
                intent.putExtra("ImageUrl", urlm6);
                startActivity(intent);
            }
        });
        men7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ServiceType.class);
                intent.putExtra("SalonType", "Mens_Section");
                intent.putExtra("Category", typm7);
                intent.putExtra("ImageUrl", urlm7);
                startActivity(intent);
            }
        });
        women1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ServiceType.class);
                intent.putExtra("SalonType", "Womens_Section");
                intent.putExtra("Category", typw1);
                intent.putExtra("ImageUrl", urlw1);
                startActivity(intent);
            }
        });
        women2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ServiceType.class);
                intent.putExtra("SalonType", "Womens_Section");
                intent.putExtra("Category", typw2);
                intent.putExtra("ImageUrl", urlw2);
                startActivity(intent);
            }
        });
        women3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ServiceType.class);
                intent.putExtra("SalonType", "Womens_Section");
                intent.putExtra("Category", typw3);
                intent.putExtra("ImageUrl", urlw3);
                startActivity(intent);
            }
        });
        women4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ServiceType.class);
                intent.putExtra("SalonType", "Womens_Section");
                intent.putExtra("Category", typw4);
                intent.putExtra("ImageUrl", urlw4);
                startActivity(intent);
            }
        });
        women5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ServiceType.class);
                intent.putExtra("SalonType", "Womens_Section");
                intent.putExtra("Category", typw5);
                intent.putExtra("ImageUrl", urlw5);
                startActivity(intent);
            }
        });
        women6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ServiceType.class);
                intent.putExtra("SalonType", "Womens_Section");
                intent.putExtra("Category", typw6);
                intent.putExtra("ImageUrl", urlw6);
                startActivity(intent);
            }
        });
        women7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ServiceType.class);
                intent.putExtra("SalonType", "Womens_Section");
                intent.putExtra("Category", typw7);
                intent.putExtra("ImageUrl", urlw7);
                startActivity(intent);
            }
        });
        women8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ServiceType.class);
                intent.putExtra("SalonType", "Womens_Section");
                intent.putExtra("Category", typw8);
                intent.putExtra("ImageUrl", urlw8);
                startActivity(intent);
            }
        });
        women9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ServiceType.class);
                intent.putExtra("SalonType", "Womens_Section");
                intent.putExtra("Category", typw9);
                intent.putExtra("ImageUrl", urlw9);
                startActivity(intent);
            }
        });

        referMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ReferAndEarn.class));
            }
        });

        Cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRegistered.equals("no")) {
                    Toast.makeText(getApplicationContext(), "You Must Log In to continue", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), ActivityPhoneVerification.class));
                } else
                    startActivity(new Intent(HomeActivity.this, CartActivity.class));
            }
        });
        seeMen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (a == 0) {
//                    for(String s:imgNameMen1){
//                        imgNameMen.add(s);
//                    }
//                    for(String s:imgUrlMen1){
//                        imgUrlMen.add(s);
//                    }
                    third_men.setVisibility(View.VISIBLE);
                    seeMoreMen.setText("See less");
                    a = 1;
                } else {
//                    for(String s:imgNameMen1){
//                        imgNameMen.remove(s);
//                    }
//                    for(String s:imgUrlMen1){
//                        imgUrlMen.remove(s);
//                    }
                    third_men.setVisibility(View.GONE);
                    seeMoreMen.setText("See all");
                    a = 0;
                }
            }
        });
        seeWomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b == 0) {
//                    for(String s:imgNameWomen1){
//                        imgNameWomen.add(s);
//                    }
//                    for(String s:imgUrlWomen1){
//                        imgUrlWomen.add(s);
//                    }
//                    womenRecyclerView.setAdapter(gridAdapterWomen);
                    third_women.setVisibility(View.VISIBLE);
                    seeMoreWomen.setText("See less");
                    b = 1;
                } else {
//                    for(String s:imgNameWomen1){
//                        imgNameWomen.remove(s);
//                    }
//                    for(String s:imgUrlWomen1){
//                        imgUrlWomen.remove(s);
//                    }
                    //womenRecyclerView.setAdapter(gridAdapterWomen);
                    third_women.setVisibility(View.GONE);
                    seeMoreWomen.setText("See all");
                    b = 0;
                }
            }
        });
//        weddingSection.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(),WeddingPreActivity.class));
//            }
//        });

        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.booking:
                        if (isRegistered.equals("no")) {
                            Toast.makeText(getApplicationContext(), "You Must Log In to continue", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), ActivityPhoneVerification.class));
                        } else {
                            startActivity(new Intent(getApplicationContext(), BookingsActivity.class));
                            overridePendingTransition(0, 0);
                        }
                        finish();
                        return true;
                    case R.id.profile:
                        if (isRegistered.equals("no")) {
                            Toast.makeText(getApplicationContext(), "You Must Log In to continue", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), ActivityPhoneVerification.class));
                        } else {
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                            overridePendingTransition(0, 0);
                        }
                        finish();
                        return true;
                    case R.id.home:
                        return true;
                }
                return false;
            }
        });
//
//        // Rotate the wheel
//
//        handleWheelAnimation(55);
        //app update code

        inAppUpdateManager = InAppUpdateManager.Builder(this, 101)
                .resumeUpdates(true)
                .mode(Constants.UpdateMode.IMMEDIATE)
                .snackBarAction("Update Available for Barbera App")
                .snackBarAction("RESTART")
                .handler(this);

        inAppUpdateManager.checkForAppUpdate();
    }

    //    private void handleWheelAnimation(int x) {
//        if(x==110){
//            x=165;
//        }
//        else if(x==165){
//            x=220;
//        }
//        else if(x==220){
//            x=275;
//        }
//        else if(x==275){
//            x=330;
//        }
//        int delayTime = 800;
//        lw.rotateWheelTo(10);
//        wheelHandler = new Handler();
//        wheelRunable = new Runnable() {
//            @Override
//            public void run() {
//                lw.rotateWheelTo(10);
//                wheelHandler.postDelayed(this, delayTime);
//            }
//        };
//        wheelHandler.postDelayed(wheelRunable, delayTime);
//
//    }
//
//    private void generateWheelItems() {
//        wheelItems = new ArrayList<>();
//        wheelItems.add(new WheelItem(Color.parseColor("#fc6c6c"), BitmapFactory.decodeResource(getResources(),
//                R.drawable.chat), "100 $"));
//        wheelItems.add(new WheelItem(Color.parseColor("#00E6FF"), BitmapFactory.decodeResource(getResources(),
//                R.drawable.coupon), "0 $"));
//        wheelItems.add(new WheelItem(Color.parseColor("#F00E6F"), BitmapFactory.decodeResource(getResources(),
//                R.drawable.ice_cream), "30 $"));
//        wheelItems.add(new WheelItem(Color.parseColor("#00E6FF"), BitmapFactory.decodeResource(getResources(),
//                R.drawable.lemonade), "6000 $"));
//        wheelItems.add(new WheelItem(Color.parseColor("#fc6c6c"), BitmapFactory.decodeResource(getResources(),
//                R.drawable.orange), "9 $"));
//        wheelItems.add(new WheelItem(Color.parseColor("#00E6FF"), BitmapFactory.decodeResource(getResources(),
//                R.drawable.shop), "20 $"));
//    }
    private void loadAnimation() {
        images = new ArrayList<Offer>();
        Retrofit retrofit1 = RetrofitClientInstanceOffers.getRetrofitInstance();
        JsonPlaceHolderApi2 jsonPlaceHolderApi22 = retrofit1.create(JsonPlaceHolderApi2.class);
        Call<OfferList> call = jsonPlaceHolderApi22.getAllOffer("Bearer " + isRegistered);
        call.enqueue(new Callback<OfferList>() {
            @Override
            public void onResponse(Call<OfferList> call, Response<OfferList> response) {
                if (response.code() == 200) {
                    List<Offer> list = response.body().getList();
                    int ind=0;
                    for (Offer offer : list) {
                        images.add(offer);
                        if(ind==0){
                            Glide.with(HomeActivity.this).load(offer.getImage()+"?" + new Date().getTime()).into(anim1);
                        }
                        else if(ind==1){
                            Glide.with(HomeActivity.this).load(offer.getImage()+"?" + new Date().getTime()).into(anim2);
                        }
                        else if(ind==2){
                            Glide.with(HomeActivity.this).load(offer.getImage()+"?" + new Date().getTime()).into(anim3);
                        }
                        else if(ind==3){
                            Glide.with(HomeActivity.this).load(offer.getImage()+"?" + new Date().getTime()).into(anim4);
                        }
                        else if(ind==4){
                            Glide.with(HomeActivity.this).load(offer.getImage()+"?" + new Date().getTime()).into(anim5);
                        }
                        else if(ind==5){
                            Glide.with(HomeActivity.this).load(offer.getImage()+"?" + new Date().getTime()).into(anim6);
                        }
                        else if(ind==6){
                            Glide.with(HomeActivity.this).load(offer.getImage()+"?" + new Date().getTime()).into(anim7);
                        }
                        else if(ind==7){
                            Glide.with(HomeActivity.this).load(offer.getImage()+"?" + new Date().getTime()).into(anim8);
                        }
                        ind++;
                    }
                    startAnimation();
                } else {
                    Toast.makeText(getApplicationContext(), "Could not load animation", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OfferList> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startAnimation() {
        rotateAnimation();
        imageAnimation();
    }

    private void rotateAnimation() {
        rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        rotateRunnable = new Runnable() {
            @Override
            public void run() {
                circularView.startAnimation(rotateAnimation);
                rotateHandler.postDelayed(this, time);
            }
        };

        rotateHandler.postDelayed(rotateRunnable, 1);
    }

    private void imageAnimation() {
        imageRunnable = new Runnable() {
            int i = 0;

            @Override
            public void run() {
                //centerImage.setImageResource(Integer.parseInt(images.get(i).getImage()));
                Glide.with(HomeActivity.this).load(images.get(i).getImage() + "?" + new Date().getTime()).into(centerImage);
                animText.setText(images.get(i).getName());
                indexCLicked = i;
                i++;
                if (i >= images.size()) {
                    i = 0;
                }
                imageHandler.postDelayed(this, time / images.size());
            }
        };
        imageHandler.postDelayed(imageRunnable, 10);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rotateHandler.removeCallbacks(rotateRunnable);
        imageHandler.removeCallbacks(imageRunnable);
    }

    private void loadTabs() {
//        tabItems=new ArrayList<>();
        progress_home.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);
        Call<SliderList> call = jsonPlaceHolderApi2.getTabs();
        call.enqueue(new Callback<SliderList>() {
            @Override
            public void onResponse(Call<SliderList> call, Response<SliderList> response) {
                if (response.code() == 200) {
                    SliderList sliderList = response.body();
                    List<SliderItem> list = sliderList.getList();
                    int i = 0;
                    for (SliderItem item : list) {
//                        tabItems.add(item);
                        if (i == 0) {
                            ll1.setVisibility(View.VISIBLE);
                            Glide.with(HomeActivity.this).load(item.getUrl() + "?" + new Date().getTime()).into(top1);
                            String[] x = item.getName().split(",");
                            topText1.setText(x[1]);
                            cat1 = x[1];
                            typ1 = x[0].replaceAll(" ", "_");
                            url1 = item.getUrl() + "?" + new Date().getTime();
                        } else if (i == 1) {
                            ll2.setVisibility(View.VISIBLE);
                            Glide.with(HomeActivity.this).load(item.getUrl() + "?" + new Date().getTime()).into(top2);
                            String[] x = item.getName().split(",");
                            topText2.setText(x[1]);
                            cat2 = x[1];
                            typ2 = x[0].replaceAll(" ", "_");
                            url2 = item.getUrl() + "?" + new Date().getTime();
                        } else if (i == 2) {
                            ll3.setVisibility(View.VISIBLE);
                            Glide.with(HomeActivity.this).load(item.getUrl() + "?" + new Date().getTime()).into(top3);
                            String[] x = item.getName().split(",");
                            topText3.setText(x[1]);
                            cat3 = x[1];
                            typ3 = x[0].replaceAll(" ", "_");
                            url3 = item.getUrl() + "?" + new Date().getTime();
                        } else if (i == 3) {
                            ll4.setVisibility(View.VISIBLE);
                            Glide.with(HomeActivity.this).load(item.getUrl() + "?" + new Date().getTime()).into(top4);
                            String[] x = item.getName().split(",");
                            topText4.setText(x[1]);
                            cat4 = x[1];
                            typ4 = x[0].replaceAll(" ", "_");
                            url4 = item.getUrl() + "?" + new Date().getTime();
                        } else {
                            ll5.setVisibility(View.VISIBLE);
                            Glide.with(HomeActivity.this).load(item.getUrl() + "?" + new Date().getTime()).into(top5);
                            String[] x = item.getName().split(",");
                            topText5.setText(x[1]);
                            cat5 = x[1];
                            typ5 = x[0].replaceAll(" ", "_");
                            url5 = item.getUrl() + "?" + new Date().getTime();
                        }
                        i++;
                    }
//                    tabRecyclerView.setAdapter(tabAdapter);
                } else {
                    Toast.makeText(getApplicationContext(), "Could not load slider", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SliderList> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadImageSlider() {
//        sliderItems=new ArrayList<>();

        Call<SliderList> call = jsonPlaceHolderApi2.getSlider();
        call.enqueue(new Callback<SliderList>() {
            @Override
            public void onResponse(Call<SliderList> call, Response<SliderList> response) {
                if (response.code() == 200) {
                    SliderList sliderList = response.body();
                    List<SliderItem> list = sliderList.getList();
                    int i = 0;
                    HashMap<Integer, String> cat = new HashMap<>();
                    HashMap<Integer, String> typ = new HashMap<>();
                    HashMap<Integer, String> url = new HashMap<>();
                    HashMap<Integer, Boolean> click = new HashMap<>();
                    for (SliderItem item : list) {
                        sliderItems.add(new SlideModel(item.getUrl() + "?" + new Date().getTime(), null));
                        cat.put(i, item.getCategory().replaceAll(" ", "_"));
                        typ.put(i, item.getTypes().replaceAll(" ", "_"));
                        url.put(i, item.getUrl() + "?" + new Date().getTime());
                        click.put(i, item.isClickable());
                        //Log.d("dd",item.isClickable()+"");
                        i++;
                    }
                    imageSlider.setImageList(sliderItems, true);
                    imageSlider.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onItemSelected(int i) {
                            if (click.get(i)) {
                                Intent intent = new Intent(HomeActivity.this, ServiceType.class);
                                intent.putExtra("SalonType", cat.get(i));
                                intent.putExtra("Category", typ.get(i));
                                intent.putExtra("ImageUrl", url.get(i));
                                startActivity(intent);
                            }
                        }
                    });
//                    sliderRecyclerView.setAdapter(sliderAdapter);
                } else {
                    Toast.makeText(getApplicationContext(), "Could not load slider", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SliderList> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void loadNumberOnCart() {
        if (isRegistered.equals("no"))
            NumberOnCartMain.setText("0");
        else {
            SharedPreferences sharedPreferences = getSharedPreferences("Count", MODE_PRIVATE);
            int count = sharedPreferences.getInt("count", 0);
            NumberOnCartMain.setText("" + count);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadNumberOnCart();
    }

    private void addWeddingGrid() {
        imgNameWed.clear();
        imgUrlWed.clear();


        Call<TypeList> call = jsonPlaceHolderApi2.getTypes("Wedding_Packages");
        call.enqueue(new Callback<TypeList>() {
            @Override
            public void onResponse(Call<TypeList> call, Response<TypeList> response) {
                if (response.code() == 200) {
                    TypeList serviceList = response.body();
                    List<String> list = serviceList.getTypeList();
                    //int z=0,f=0;
                    if (list.size() != 0) {
                        for (String item : list) {
                            imgNameWed.add(item);
                            item = item.replaceAll(" ", "_");
                            imgUrlWed.add(imagebase + "Wedding_Packages" + item + "?" + new Date().getTime());
                        }
                        weddingRecyclerView.setAdapter(gridAdapterWed);
                        LinearLayoutManager tabLlm = new LinearLayoutManager(getApplicationContext());
                        tabLlm.setOrientation(RecyclerView.HORIZONTAL);
                        weddingRecyclerView.setLayoutManager(tabLlm);
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Could not get services", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TypeList> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        weddingRecyclerView.setAdapter(gridAdapterWed);
    }

    private void addWomenGrid() {
        imgNameWomen.clear();
        imgUrlWomen.clear();

        Call<TypeList> call = jsonPlaceHolderApi2.getTypes("Womens_Section");
        call.enqueue(new Callback<TypeList>() {
            @Override
            public void onResponse(Call<TypeList> call, Response<TypeList> response) {
                if (response.code() == 200) {
                    TypeList serviceList = response.body();
                    List<String> list = serviceList.getTypeList();
                    int z = 0, f = 0;
                    if (list.size() != 0) {
                        for (String item : list) {
                            if (z < 6) {
                                if (z == 0) {
                                    String url = imagebase + "Womens_Section" + item.replaceAll(" ", "_") + "?" + new Date().getTime();
                                    Glide.with(HomeActivity.this).load(url).into(women1);
                                    womenText1.setText(item);
                                    typw1 = item;
                                    urlw1 = url;
                                } else if (z == 1) {
                                    String url = imagebase + "Womens_Section" + item.replaceAll(" ", "_") + "?" + new Date().getTime();
                                    Glide.with(HomeActivity.this).load(url).into(women2);
                                    typw2 = item;
                                    urlw2 = url;
                                    womenText2.setText(item);
                                } else if (z == 2) {
                                    String url = imagebase + "Womens_Section" + item.replaceAll(" ", "_") + "?" + new Date().getTime();
                                    Glide.with(HomeActivity.this).load(url).into(women3);
                                    typw3 = item;
                                    urlw3 = url;
                                    womenText3.setText(item);
                                } else if (z == 3) {
                                    String url = imagebase + "Womens_Section" + item.replaceAll(" ", "_") + "?" + new Date().getTime();
                                    Glide.with(HomeActivity.this).load(url).into(women4);
                                    typw4 = item;
                                    urlw4 = url;
                                    womenText4.setText(item);
                                } else if (z == 4) {
                                    String url = imagebase + "Womens_Section" + item.replaceAll(" ", "_") + "?" + new Date().getTime();
                                    Glide.with(HomeActivity.this).load(url).into(women5);
                                    typw5 = item;
                                    urlw5 = url;
                                    womenText5.setText(item);
                                } else if (z == 5) {
                                    String url = imagebase + "Womens_Section" + item.replaceAll(" ", "_") + "?" + new Date().getTime();
                                    Glide.with(HomeActivity.this).load(url).into(women6);
                                    typw6 = item;
                                    urlw6 = url;
                                    womenText6.setText(item);
                                }
                                z++;
                            } else {
                                if (z == 6) {
                                    String url = imagebase + "Womens_Section" + item.replaceAll(" ", "_") + "?" + new Date().getTime();
                                    Glide.with(HomeActivity.this).load(url).into(women7);
                                    typw7 = item;
                                    urlw7 = url;
                                    womenText7.setText(item);
                                } else if (z == 7) {
                                    String url = imagebase + "Womens_Section" + item.replaceAll(" ", "_") + "?" + new Date().getTime();
                                    Glide.with(HomeActivity.this).load(url).into(women8);
                                    typw8 = item;
                                    urlw8 = url;
                                    womenText8.setText(item);
                                } else if (z == 8) {
                                    String url = imagebase + "Womens_Section" + item.replaceAll(" ", "_") + "?" + new Date().getTime();
                                    Glide.with(HomeActivity.this).load(url).into(women9);
                                    typw9 = item;
                                    urlw9 = url;
                                    womenText9.setText(item);
                                }
                                z++;
                                f = 1;
                            }
                        }
                        if (f == 0) {
                            seeWomen.setEnabled(false);
                            seeMoreWomen.setVisibility(View.GONE);
                            seeWomen.setVisibility(View.GONE);
                        } else {
                            seeWomen.setEnabled(true);
                            seeMoreWomen.setVisibility(View.VISIBLE);
                            seeMoreWomen.setText("See all");
                            seeWomen.setVisibility(View.VISIBLE);
                        }
                    }
                    //womenRecyclerView.setAdapter(gridAdapterWomen);
                    //gridView.setAdapter(adapter);
                    progress_home.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(), "Could not get services", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TypeList> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addMenGrid() {
        imgNameMen.clear();
        imgUrlMen.clear();
        Call<TypeList> call = jsonPlaceHolderApi2.getTypes("Mens_Section");
        call.enqueue(new Callback<TypeList>() {
            @Override
            public void onResponse(Call<TypeList> call, Response<TypeList> response) {
                if (response.code() == 200) {
                    TypeList serviceList = response.body();
                    List<String> list = serviceList.getTypeList();
                    int z = 0, f = 0;
                    if (list.size() != 0) {
                        for (String item : list) {
                            if (z < 6) {
                                if (z == 0) {
                                    String url = imagebase + "Mens_Section" + item.replaceAll(" ", "_") + "?" + new Date().getTime();
                                    Glide.with(HomeActivity.this).load(url).into(men1);
                                    menText1.setText(item);
                                    Log.d("url", url);
                                    typm1 = item;
                                    urlm1 = url;
                                } else if (z == 1) {
                                    String url = imagebase + "Mens_Section" + item.replaceAll(" ", "_") + "?" + new Date().getTime();
                                    Glide.with(HomeActivity.this).load(url).into(men2);
                                    menText2.setText(item);
                                    Log.d("url", url);
                                    typm2 = item;
                                    urlm2 = url;
                                } else if (z == 2) {
                                    String url = imagebase + "Mens_Section" + item.replaceAll(" ", "_") + "?" + new Date().getTime();
                                    Glide.with(HomeActivity.this).load(url).into(men3);
                                    menText3.setText(item);
                                    Log.d("url", url);
                                    typm3 = item;
                                    urlm3 = url;
                                } else if (z == 3) {
                                    String url = imagebase + "Mens_Section" + item.replaceAll(" ", "_") + "?" + new Date().getTime();
                                    Glide.with(HomeActivity.this).load(url).into(men4);
                                    Log.d("url", url);
                                    menText4.setText(item);
                                    typm4 = item;
                                    urlm4 = url;
                                } else if (z == 4) {
                                    String url = imagebase + "Mens_Section" + item.replaceAll(" ", "_") + "?" + new Date().getTime();
                                    Log.d("url", url);
                                    Glide.with(HomeActivity.this).load(url).into(men5);
                                    menText5.setText(item);
                                    typm5 = item;
                                    urlm5 = url;
                                } else if (z == 5) {
                                    String url = imagebase + "Mens_Section" + item.replaceAll(" ", "_") + "?" + new Date().getTime();
                                    Glide.with(HomeActivity.this).load(url).into(men6);
                                    menText6.setText(item);
                                    Log.d("url", url);
                                    typm6 = item;
                                    urlm6 = url;
                                }
                                z++;
                            } else {
                                f = 1;
                                if (z == 6) {
                                    String url = imagebase + "Mens_Section" + item.replaceAll(" ", "_") + "?" + new Date().getTime();
                                    Glide.with(HomeActivity.this).load(url).into(men7);
                                    Log.d("url", url);
                                    typm7 = item;
                                    urlm7 = url;
                                    menText7.setText(item);
                                }
                                z++;
//                                imgNameMen1.add(item);
//                                item = item.replaceAll(" ", "_");
//                                //Log.d("item",item);
//                                imgUrlMen1.add(imagebase + "Mens_Section" + item);
                            }
                        }
                        if (f == 0) {
                            seeMen.setEnabled(false);
                            seeMoreMen.setVisibility(View.GONE);
                            seeMen.setVisibility(View.GONE);
                        } else {
                            seeMen.setEnabled(true);
                            seeMoreMen.setVisibility(View.VISIBLE);
                            seeMen.setVisibility(View.VISIBLE);
                            seeMoreMen.setText("See all");
                        }
                    }
                    //menRecyclerView.setAdapter(gridAdapterMen);
                    //gridView.setAdapter(adapter);
                } else {
                    Toast.makeText(getApplicationContext(), "Could not get services", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TypeList> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

//        imgName.add("Hair Cut");
//        imgName.add("Shaving");
//        imgName.add("Hair Color");
//        imgName.add("Facial");
//        imgName.add("Head Massage");
//        imgName.add("Bleech");

//        imgUrl.add("https://i.ibb.co/t4z5Vqp/image.png");
//        imgUrl.add("https://i.ibb.co/7zYTRbs/image.png");
//        imgUrl.add("https://i.ibb.co/mGqJLmJ/image.png");
//        imgUrl.add("https://i.ibb.co/PFWzrPb/image.png");
//        imgUrl.add("https://i.ibb.co/smdS7FT/image.png");
//        imgUrl.add("https://i.ibb.co/Ht4gYjf/image.png");


    }

    @Override
    public void onInAppUpdateError(int code, Throwable error) {

    }

    @Override
    public void onInAppUpdateStatus(InAppUpdateStatus status) {
        if (status.isDownloaded()) {
            View view = getWindow().getDecorView().findViewById(android.R.id.content);
            Snackbar snackbar = Snackbar.make(view, "Update Available for Barbera App", Snackbar.LENGTH_INDEFINITE);

            snackbar.setAction("", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inAppUpdateManager.completeUpdate();
                }
            });
            snackbar.show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        wheelHandler.removeCallbacks(wheelRunable);
//    }
}
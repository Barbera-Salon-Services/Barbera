package com.barbera.barberaconsumerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.barbera.barberaconsumerapp.Utils.ServiceItem;
import com.barbera.barberaconsumerapp.Utils.TypeList;
import com.barbera.barberaconsumerapp.network_aws.JsonPlaceHolderApi2;
import com.barbera.barberaconsumerapp.network_aws.RetrofitClientInstanceService;
import com.barbera.barberaconsumerapp.network_aws.RetrofitClientInstanceUser;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SubCategoryActivity extends AppCompatActivity {
    private ListView sublistview;
    public static ProgressBar progressBarOnSubCategory;
    private String Category;
    private String CategoryIMage;
    private ImageView serviceimage;
    public static String salontype;
    private String collecton;
    private List<String> subCategoryList=new ArrayList<>();
    private SubCategoryAdapter subCategoryAdapter;
    private String serViceType,token;
    private String subCategoryImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_service_activity);

        Intent intent=getIntent();
        Category=intent.getStringExtra("Category");
        CategoryIMage=intent.getStringExtra("CategoryIMage");
        salontype=intent.getStringExtra("SalonType");
        collecton=intent.getStringExtra("Collection");
        serViceType=intent.getStringExtra("ServiceType");
        sublistview=(ListView) findViewById(R.id.new_service_recycler_view);
        TextView title=(TextView)findViewById(R.id.new_service_title);
        progressBarOnSubCategory=(ProgressBar)findViewById(R.id.new_service_progress_bar);
        ImageView cart=(ImageView)findViewById(R.id.new_service_cart);
        serviceimage=(ImageView)findViewById(R.id.new_service_image);
        subCategoryAdapter=new SubCategoryAdapter(subCategoryList);
        LinearLayout cartAndBookLayout=(LinearLayout)findViewById(R.id.addANdbookLayout);

        cartAndBookLayout.setVisibility(View.GONE);
        cart.setVisibility(View.GONE);

        Retrofit retrofit = RetrofitClientInstanceService.getRetrofitInstance();
        JsonPlaceHolderApi2 jsonPlaceHolderApi2=retrofit.create(JsonPlaceHolderApi2.class);
        SharedPreferences preferences=getSharedPreferences("Token",MODE_PRIVATE);
        token = preferences.getString("token","no");

      /* cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FirebaseAuth.getInstance().getCurrentUser()==null){
                    Toast.makeText(getApplicationContext(),"You Must Log In to continue",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                }
                else {
                    startActivity(new Intent(SubCategoryActivity.this, CartActivity.class));
                }
            }
        });*/

        title.setText(Category);

        Glide.with(getApplicationContext()).load(CategoryIMage)
                .apply(new RequestOptions().placeholder(R.drawable.logo)).into(serviceimage);

        if(subCategoryList.size()==0){
            progressBarOnSubCategory.setVisibility(View.VISIBLE);
            Call<TypeList> call=jsonPlaceHolderApi2.getSubTypes(salontype,new ServiceItem(null,null,null,null,null,null,Category,
                    false,null,false,null),"Bearer "+token);
            call.enqueue(new Callback<TypeList>() {
                @Override
                public void onResponse(Call<TypeList> call, Response<TypeList> response) {
                    if(response.code()==200){
                        TypeList serviceList=response.body();
                        List<String> list=serviceList.getTypeList();
                            for(String item:list){
                                subCategoryList.add(item);
                            }
                            sublistview.setAdapter(subCategoryAdapter);
                            progressBarOnSubCategory.setVisibility(View.INVISIBLE);

                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Could not load services",Toast.LENGTH_SHORT).show();
                        progressBarOnSubCategory.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<TypeList> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                    progressBarOnSubCategory.setVisibility(View.INVISIBLE);
                }
            });
        }
        sublistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SubCategoryActivity.this, ParlourActivity.class);
                intent.putExtra("Category", Category);
                intent.putExtra("CategoryIMage",CategoryIMage);
                intent.putExtra("SalonType",salontype);
                intent.putExtra("Collection",collecton);
                intent.putExtra("SubCategDoc",subCategoryList.get(position));
                intent.putExtra("ServiceType",serViceType);
                startActivity(intent);
            }
        });
    }
}
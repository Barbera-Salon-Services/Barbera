package com.barbera.barberaconsumerapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.barbera.barberaconsumerapp.Bookings.BookingPage;
import com.barbera.barberaconsumerapp.Utils.ServiceItem;
import com.barbera.barberaconsumerapp.network_aws.JsonPlaceHolderApi2;
import com.barbera.barberaconsumerapp.network_aws.RetrofitClientInstanceCart;
import com.barbera.barberaconsumerapp.network_aws.Success;
import com.barbera.barberaconsumerapp.network_aws.SuccessReturn;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static java.lang.Integer.parseInt;

public class MenHorizontalAdapter extends RecyclerView.Adapter {
    private List<ServiceItem> HorizontalserviceList;
    private Context activity;
    private int flag;

    public MenHorizontalAdapter(List<ServiceItem> horizontalserviceList, Context activity, int flag) {
        HorizontalserviceList = horizontalserviceList;
        this.activity=activity;
        this.flag=flag;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.newservice_design,parent,false);
        return new MenItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String title=HorizontalserviceList.get(position).getName();
        //String imgResource=HorizontalserviceList.get(position).getImageId();
        int price=HorizontalserviceList.get(position).getPrice();
        int cutPrice=HorizontalserviceList.get(position).getCutprice();
        int TIME=HorizontalserviceList.get(position).getTime();
        String id=HorizontalserviceList.get(position).getId();
        //Toast.makeText(activity,title+" "+id,Toast.LENGTH_SHORT).show();

        ((MenItemViewHolder)holder).setDetails(title,null,price,cutPrice,position,TIME,id);

    }

    @Override
    public int getItemCount() {
        return HorizontalserviceList.size();
    }

    class MenItemViewHolder extends RecyclerView.ViewHolder{
        private ImageView photo;
        private TextView title;
        private TextView price;
        private TextView cutPrice;
        private Button add;
        private Button bookNow;
        private TextView time;

        public MenItemViewHolder(@NonNull View itemView) {

            super(itemView);

            photo=(ImageView) itemView.findViewById(R.id.service_image);
            title=(TextView) itemView.findViewById(R.id.service_title);
            price=(TextView) itemView.findViewById(R.id.servicePrice);
            cutPrice=(TextView) itemView.findViewById(R.id.Service_cutPrice);
            add=(Button) itemView.findViewById(R.id.add_to_cart);
            bookNow=(Button)itemView.findViewById(R.id.book_now_button);
            time=(TextView)itemView.findViewById(R.id.serviceTime);
        }

        private void setDetails(String Title,String imgLink,int Price,int CutPrice,final int position,int iTime,String id){
            final ServiceItem adapterList=HorizontalserviceList.get(position);
            title.setText(Title);
            price.setText("Rs "+Price);
            cutPrice.setText("Rs "+CutPrice);
            time.setText(iTime+" Min");
            //Toast.makeText(activity,id,Toast.LENGTH_SHORT).show();
//            Glide.with(itemView.getContext()).load(imgLink)
//                    .apply(new RequestOptions().placeholder(R.drawable.logo)).into(photo);
            Retrofit retrofit = RetrofitClientInstanceCart.getRetrofitInstance();
            JsonPlaceHolderApi2 jsonPlaceHolderApi2 = retrofit.create(JsonPlaceHolderApi2.class);
            SharedPreferences preferences = activity.getSharedPreferences("Token", activity.MODE_PRIVATE);
            String token = preferences.getString("token", "no");

            add.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onClick(View v) {
                    if(token.equals("no")){
                        Toast.makeText(activity,"You Must Log In to continue",Toast.LENGTH_LONG).show();
                        activity.startActivity(new Intent(activity,SecondScreen.class));
                    }
                    else {
                            final ProgressDialog progressDialog = new ProgressDialog(activity);
                            progressDialog.show();
                            progressDialog.setContentView(R.layout.progress_dialog);
                            progressDialog.setCancelable(false);
                            List<String> idList = new ArrayList<>();
                            idList.add(id);
                            Call<SuccessReturn> call=jsonPlaceHolderApi2.addToCart(new Success(idList),"Bearer "+token);
                            call.enqueue(new Callback<SuccessReturn>() {
                                @Override
                                public void onResponse(Call<SuccessReturn> call, Response<SuccessReturn> response) {
                                    if(response.code()==200){
                                        SuccessReturn success=response.body();
                                        int count = success.getCount();
                                        SharedPreferences sharedPreferences= activity.getSharedPreferences("Count",activity.MODE_PRIVATE);
                                        SharedPreferences.Editor editor=sharedPreferences.edit();
                                        editor.putInt("count",count);
                                        editor.apply();
                                        if(success.isSuccess()){
                                            progressDialog.dismiss();
                                            Toast.makeText(activity,"Added to cart",Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            progressDialog.dismiss();
                                            Toast.makeText(activity,success.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else{
                                        progressDialog.dismiss();
                                        Toast.makeText(activity,"Could not add to cart",Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<SuccessReturn> call, Throwable t) {
                                    progressDialog.dismiss();
                                    Toast.makeText(activity,t.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });
                    }
                }
            });

            bookNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(token.equals("no")){
                        Toast.makeText(itemView.getContext(),"You Must Log In to continue",Toast.LENGTH_LONG).show();
                        itemView.getContext().startActivity(new Intent(itemView.getContext(),SecondScreen.class));
                    }
                    else {
//                        activity.startActivity(new Intent(activity,MapSearchActivity.class));
                        String ordersummary;
                        if(flag==0){
                            ordersummary="(men)"+HorizontalserviceList.get(position).getName()+"  Rs"+HorizontalserviceList.get(position).getPrice();
                        }
                        else{
                            ordersummary="(women)"+HorizontalserviceList.get(position).getName()+"  Rs"+HorizontalserviceList.get(position).getPrice();
                        }

                        int time= HorizontalserviceList.get(position).getTime();
                        Intent intent=new Intent(activity, BookingPage.class);
                        intent.putExtra("BookingType","trend");
                        intent.putExtra("Booking Amount", HorizontalserviceList.get(position).getPrice());
                        intent.putExtra("Order Summary",ordersummary);
                        intent.putExtra("Time",time);
                        //Toast.makeText(itemView.getContext(),"scascsnsvni", Toast.LENGTH_SHORT).show();
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                    }
                }
            });

        }
    }
}

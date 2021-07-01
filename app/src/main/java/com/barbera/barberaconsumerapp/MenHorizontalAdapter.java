package com.barbera.barberaconsumerapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.barbera.barberaconsumerapp.Utils.ServiceItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String price=HorizontalserviceList.get(position).getPrice();
        String cutPrice=HorizontalserviceList.get(position).getDiscount();
        String TIME=HorizontalserviceList.get(position).getTime();

        //((MenItemViewHolder)holder).setDetails(title,imgResource,price,cutPrice,position,TIME);

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

        private void setDetails(String Title,String imgLink,String Price,String CutPrice,final int position,String iTime){
            final ServiceItem adapterList=HorizontalserviceList.get(position);
            title.setText(Title);
            price.setText("Rs "+Price);
            cutPrice.setText("Rs "+CutPrice);
            time.setText(iTime+" Min");
            Glide.with(itemView.getContext()).load(imgLink)
                    .apply(new RequestOptions().placeholder(R.drawable.logo)).into(photo);

            add.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onClick(View v) {
                    if(FirebaseAuth.getInstance().getCurrentUser()==null){
                        Toast.makeText(itemView.getContext(),"You Must Log In to continue",Toast.LENGTH_LONG).show();
                        itemView.getContext().startActivity(new Intent(itemView.getContext(),SecondScreen.class));
                    }
                    else{
                        if(!dbQueries.cartList.contains(adapterList.getId())){
                            final ProgressDialog progressDialog=new ProgressDialog(itemView.getContext());
                            progressDialog.show();
                            // SplashActivity.progressText.setText("Adding Service To Cart");
                            progressDialog.setContentView(R.layout.progress_dialog);
                            progressDialog.setCancelable(false);
                            add.setEnabled(false);
                            DocumentReference documentReference=   FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .collection("UserData").document("MyCart");
                            Map<String,Object> cartData=new HashMap<>();
                            cartData.put("service_id_"+String.valueOf(dbQueries.cartList.size()+1),adapterList.getId());
                            cartData.put("service_id_"+String.valueOf(dbQueries.cartList.size()+1)+"_type",adapterList.getType());
                            cartData.put("cart_list_size",(long)(dbQueries.cartList.size()+1));
                            documentReference.update(cartData)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                dbQueries.cartList.add(adapterList.getId());
                                                dbQueries.cartItemModelList.clear();
                                                MainActivity.loadNumberOnCart();
                                                CartActivity.updateCartItemModelList();
                                                Toast.makeText(itemView.getContext(),"Service Added to Cart",Toast.LENGTH_SHORT).show();
                                                add.setEnabled(true);
                                                progressDialog.dismiss();
                                            }
                                            else {
                                                Toast.makeText(itemView.getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                                add.setEnabled(true);
                                            }
                                        }
                                    });
                        }
                        else
                            Toast.makeText(itemView.getContext(),"Already Added to Cart",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            bookNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(FirebaseAuth.getInstance().getCurrentUser()==null){
                        Toast.makeText(itemView.getContext(),"You Must Log In to continue",Toast.LENGTH_LONG).show();
                        itemView.getContext().startActivity(new Intent(itemView.getContext(),SecondScreen.class));
                    }
                    else {
                        String ordersummary;
                        if(flag==0){
                            ordersummary="(men)"+HorizontalserviceList.get(position).getName()+"  Rs"+HorizontalserviceList.get(position).getPrice();
                        }
                        else{
                            ordersummary="(women)"+HorizontalserviceList.get(position).getName()+"  Rs"+HorizontalserviceList.get(position).getPrice();
                        }

                        int time= parseInt(HorizontalserviceList.get(position).getTime());
                        Intent intent=new Intent(activity,BookingPage.class);
                        intent.putExtra("BookingType","trend");
                        intent.putExtra("Booking Amount", parseInt(HorizontalserviceList.get(position).getPrice()));
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

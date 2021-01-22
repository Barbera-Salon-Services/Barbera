package com.barbera.barberaconsumerapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeddingAdapter extends RecyclerView.Adapter {
    private List<WeddingModel> weddingList=new ArrayList<>();

    public WeddingAdapter(List<WeddingModel> weddingList) {
        this.weddingList = weddingList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.wedding_design,parent,false);
        return new WeddingItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String Packagename=weddingList.get(position).getPackageName();
        String Packageservices=weddingList.get(position).getPackageContent();
        String Packageprice=weddingList.get(position).getPackagePrice();

        ((WeddingItemViewHolder)holder).setDetails(Packagename,Packageservices,Packageprice,position);

    }

    @Override
    public int getItemCount() {
        return weddingList.size();
    }
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class WeddingItemViewHolder extends RecyclerView.ViewHolder{
        private TextView Name;
        private TextView services;
        private TextView price;
        private Button book;
        private Button addToCart;

        public WeddingItemViewHolder(@NonNull View itemView) {
            super(itemView);

            Name=(TextView)itemView.findViewById(R.id.package_name);
            services=(TextView)itemView.findViewById(R.id.wedding_package_details);
            price=(TextView)itemView.findViewById(R.id.wedding_package_price);
            book=(Button)itemView.findViewById(R.id.wedding_package_booking);
            addToCart=(Button)itemView.findViewById(R.id.wedding_package_to_cart);
        }

        public void setDetails(String packagename, String packageservices, String packageprice, final int position) {
            Name.setText(packagename);
            services.setText(packageservices);
            price.setText("Rs "+packageprice);

            book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(FirebaseAuth.getInstance().getCurrentUser()==null){
                        Toast.makeText(itemView.getContext(),"You Must Log In to continue",Toast.LENGTH_LONG).show();
                        itemView.getContext().startActivity(new Intent(itemView.getContext(),SecondScreen.class));
                    }
                    else {
                       // BookingPage.OrderSummary = weddingList.get(position).getPackageName();
                        //BookingPage.BookingTotalAmount = weddingList.get(position).getPackagePrice();
                        //Intent intent = new Intent(itemView.getContext(), BookingPage.class);

                        //intent.putExtra("Position",position);
                        //itemView.getContext().startActivity(intent);
                        Intent intent=new Intent(itemView.getContext(),BookingPage.class);
                        intent.putExtra("BookingType", "Wedding");
                        intent.putExtra("Booking Amount",weddingList.get(position).getPackagePrice());
                        intent.putExtra("Order Summary",weddingList.get(position).getPackageName());
                        itemView.getContext().startActivity(intent);
                    }
                }
            });
            addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(FirebaseAuth.getInstance().getCurrentUser()==null){
                        Toast.makeText(itemView.getContext(),"You Must Log In to continue",Toast.LENGTH_LONG).show();
                        itemView.getContext().startActivity(new Intent(itemView.getContext(),SecondScreen.class));
                    }
                    else{
                        if(!dbQueries.cartList.contains(weddingList.get(position).getPackageName())){
                            final ProgressDialog progressDialog=new ProgressDialog(itemView.getContext());
                            progressDialog.show();
                            progressDialog.setContentView(R.layout.progress_dialog);
                            progressDialog.setCancelable(false);
                            DocumentReference documentReference=   FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .collection("UserData").document("MyCart");
                            Map<String,Object> cartData=new HashMap<>();
                            cartData.put("service_id_"+String.valueOf(dbQueries.cartList.size()+1),weddingList.get(position).getPackageName());
                            cartData.put("service_id_"+String.valueOf(dbQueries.cartList.size()+1)+"_type",weddingList.get(position).getType());
                            cartData.put("cart_list_size",(long)(dbQueries.cartList.size()+1));
                            documentReference.update(cartData)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                dbQueries.cartList.add(weddingList.get(position).getPackageName());
                                                dbQueries.cartItemModelList.clear();
                                                CartActivity.updateCartItemModelList();
                                                Toast.makeText(itemView.getContext(),"Service Added to Cart",Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                            else {
                                                Toast.makeText(itemView.getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        }
                                    });
                        }
                        else
                            Toast.makeText(itemView.getContext(),"Already Added to Cart",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }
}

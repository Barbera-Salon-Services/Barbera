package com.barbera.barberaconsumerapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

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
   // private Context context;
   // private LayoutInflater layoutInflater;

    public WeddingAdapter(List<WeddingModel> weddingList) {
        this.weddingList = weddingList;
    }
      @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.new_wedding_piece,parent,false);
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
        private RelativeLayout Layout1;
        private RelativeLayout Layout2;
        private RelativeLayout Layout3;
        private RelativeLayout Layout4;
        private RelativeLayout Layout5;
        private TextView sitting1;
        private TextView sitting2;
        private TextView sitting3;
        private TextView sitting4;
        private TextView sitting5;
        private ImageView arrowback;
        private ImageView arrowForward;

        public WeddingItemViewHolder(@NonNull View itemView) {
            super(itemView);

            Name=(TextView)itemView.findViewById(R.id.package_heading);
            //services=(TextView)itemView.findViewById(R.id.wedding_package_details);
            price=(TextView)itemView.findViewById(R.id.package_price_new_wedding);
            book=(Button)itemView.findViewById(R.id.wedding_package_booking);
            addToCart=(Button)itemView.findViewById(R.id.wedding_package_to_cart);
           /* Layout1=(RelativeLayout)itemView.findViewById(R.id.layout_1_sitting);
            Layout2=(RelativeLayout)itemView.findViewById(R.id.layout_2_sitting);
            Layout3=(RelativeLayout)itemView.findViewById(R.id.layout_3_sitting);*/
            Layout4=(RelativeLayout)itemView.findViewById(R.id.layout_4_sitting);
            Layout5=(RelativeLayout)itemView.findViewById(R.id.layout_5_sitting);
            sitting1=(TextView)itemView.findViewById(R.id.sitting_1_content);
            sitting2=(TextView)itemView.findViewById(R.id.sitting_2_content);
            sitting3=(TextView)itemView.findViewById(R.id.sitting_3_content);
            sitting4=(TextView)itemView.findViewById(R.id.sitting_4_content);
            sitting5=(TextView)itemView.findViewById(R.id.sitting_5_content);
            arrowback=(ImageView)itemView.findViewById(R.id.arrowBack);
            arrowForward=(ImageView)itemView.findViewById(R.id.arrowForward);

        }

        public void setDetails(String packagename, String packageservices, String packageprice, final int position) {
            Name.setText(packagename);
            //services.setText(packageservices);
            price.setText("Rs "+packageprice);
            sitting1.setText(weddingList.get(position).sittings.get(0));
            sitting2.setText(weddingList.get(position).sittings.get(1));
            sitting3.setText(weddingList.get(position).sittings.get(2));
            if(weddingList.get(position).sittings.size()>3){
                Layout4.setVisibility(View.VISIBLE);
                Layout5.setVisibility(View.VISIBLE);
                sitting4.setText(weddingList.get(position).sittings.get(3));
                sitting5.setText(weddingList.get(position).sittings.get(4));
            }
            if(position==0) {
                arrowback.setVisibility(View.INVISIBLE);
                arrowForward.setVisibility(View.VISIBLE);
            }
            else if(position+1==weddingList.size()) {
                arrowForward.setVisibility(View.INVISIBLE);
                arrowback.setVisibility(View.VISIBLE);
            }
            else{
                arrowback.setVisibility(View.VISIBLE);
                arrowForward.setVisibility(View.VISIBLE);
            }




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
                        String ordersummary=weddingList.get(position).getPackageName()+"   Rs"+weddingList.get(position).getPackagePrice();
                        Intent intent=new Intent(itemView.getContext(),BookingPage.class);
                        intent.putExtra("BookingType", "Wedding");
                        intent.putExtra("Booking Amount",Integer.parseInt(weddingList.get(position).getPackagePrice()));
                        intent.putExtra("Order Summary",ordersummary);
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
                            DocumentReference documentReference=   FirebaseFirestore.getInstance().collection("Users").
                                    document(FirebaseAuth.getInstance().getCurrentUser().getUid())
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

package com.barbera.barberaconsumerapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
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

import com.barbera.barberaconsumerapp.Bookings.BookingPage;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WeddingAdapter extends RecyclerView.Adapter {
    private List<WeddingModel> weddingList=new ArrayList<>();
    private Context context;
    // private Context context;
    // private LayoutInflater layoutInflater;

    public WeddingAdapter(List<WeddingModel> weddingList,Context context) {
        this.weddingList = weddingList;
        this.context = context;
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
        int Packageprice=weddingList.get(position).getPackagePrice();

        List<String> list= Arrays.asList(Packageservices.split("/n"));
        for(String s:list){
            Log.d("str",s);
        }

        ((WeddingItemViewHolder)holder).setDetails(Packagename,list,Packageprice,position);

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

        public void setDetails(String packagename, List<String> packageservices, int packageprice, final int position) {
            Name.setText(packagename);
            //services.setText(packageservices);
            price.setText("Rs "+packageprice);
            sitting1.setText(packageservices.get(0));
            sitting2.setText(packageservices.get(1));
            sitting3.setText(packageservices.get(2));
            if(packageservices.size()>3){
                Layout4.setVisibility(View.VISIBLE);
                Layout5.setVisibility(View.VISIBLE);
                sitting4.setText(packageservices.get(3));
                sitting5.setText(packageservices.get(4));
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
                        String ordersummary="("+WeddingActivity.weddingType+")"+weddingList.get(position).getPackageName()+"   Rs"+weddingList.get(position).getPackagePrice();
                        Intent intent=new Intent(itemView.getContext(), BookingPage.class);
                        intent.putExtra("BookingType", "Wedding");
                        intent.putExtra("Booking Amount",weddingList.get(position).getPackagePrice());
                        intent.putExtra("Order Summary",ordersummary);
                        itemView.getContext().startActivity(intent);
                    }
                }
            });

            addToCart.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "+916377894199"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            });
        }
    }
}
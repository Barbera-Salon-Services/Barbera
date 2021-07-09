package com.barbera.barberaconsumerapp.Profile;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.barbera.barberaconsumerapp.Bookings.BookingPage;
import com.barbera.barberaconsumerapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class MyCouponAdapter extends RecyclerView.Adapter {
    private List<CouponItemModel> couponItemModels;

    public MyCouponAdapter(List<CouponItemModel> couponItemModels) {
        this.couponItemModels = couponItemModels;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.newservice_design,parent,false);
        return new CouponHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String icon=couponItemModels.get(position).getImageId();
        String name=couponItemModels.get(position).getServiceName();
        String price=couponItemModels.get(position).getServicePrice();

        ((CouponHolder)holder).setCouponDetails(icon,name,price,position);

    }

    @Override
    public int getItemCount() {
        return couponItemModels.size();
    }

    class CouponHolder extends RecyclerView.ViewHolder{
        private ImageView serviceImage;
        private TextView servicename;
        private TextView couponservicePrice;
        private Button booknow;

        public CouponHolder(@NonNull View itemView) {
            super(itemView);
            serviceImage=(ImageView)itemView.findViewById(R.id.service_image);
            servicename=(TextView)itemView.findViewById(R.id.service_title);
            couponservicePrice=(TextView)itemView.findViewById(R.id.servicePrice);
            booknow=(Button)itemView.findViewById(R.id.book_now_button);
            View lineView=itemView.findViewById(R.id.line);
            lineView.setVisibility(View.GONE);
            ImageView timeImage=itemView.findViewById(R.id.serviceTimeImage);
            timeImage.setVisibility(View.GONE);
            Button addtocart=itemView.findViewById(R.id.add_to_cart);
            addtocart.setVisibility(View.GONE);
        }

        public void setCouponDetails(String icon, String name, String price, final int position) {
            servicename.setText(name);
            couponservicePrice.setText("Rs "+price);
            Glide.with(itemView.getContext()).load(icon)
                    .apply(new RequestOptions().placeholder(R.drawable.logo)).into(serviceImage);

            booknow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String OrderSummary="";
                    OrderSummary+="("+couponItemModels.get(position).getType()+")"+couponItemModels.get(position).getServiceName()+
                            "\t\t\tRs "+couponItemModels.get(position).getServicePrice();
                    //BookingPage.BookingTotalAmount=couponItemModels.get(position).getServicePrice();

                    Intent intent=new Intent(itemView.getContext(), BookingPage.class);
                    intent.putExtra("BookingType","Coupon");
                    intent.putExtra("Position",position);
                    intent.putExtra("Booking Amount",Integer.parseInt(couponItemModels.get(position).getServicePrice()));
                    intent.putExtra("Order Summary",OrderSummary);
                    itemView.getContext().startActivity(intent);
                }
            });


        }
    }
}

package com.barbera.barberaconsumerapp;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class SpecialOfferAdapter extends RecyclerView.Adapter<SpecialOfferAdapter.ViewHolder> {

    private List<SpecialOfferData> specialOfferDataList;
    private Context context;

    public SpecialOfferAdapter(List<SpecialOfferData> specialOfferDataList, Context context) {
        this.specialOfferDataList = specialOfferDataList;
        this.context = context;
    }


    @NonNull
    @Override
    public SpecialOfferAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item_special_offers, parent, false);
        return new SpecialOfferAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpecialOfferAdapter.ViewHolder holder, int position) {
        SpecialOfferData specialOfferData = specialOfferDataList.get(position);
        holder.offerName.setText(specialOfferData.getOfferName());
        holder.time.setText(specialOfferData.getTime());
        holder.cancelledPrice.setPaintFlags(holder.cancelledPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.cancelledPrice.setText(specialOfferData.getCancelledPrice());
        holder.finalPrice.setText(specialOfferData.getFinalPrice());
        holder.offerDescription.setText(specialOfferData.getOfferDescription());
        Glide.with(context).load(specialOfferData.getOfferImageUrl()).into(holder.offerImage);

        holder.bookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Booking action for special offer
            }
        });
    }

    @Override
    public int getItemCount() {
        return specialOfferDataList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView offerName, time, cancelledPrice, finalPrice, offerDescription, bookNow;
        ImageView offerImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            offerName = itemView.findViewById(R.id.offer_name);
            time = itemView.findViewById(R.id.offer_time);
            cancelledPrice = itemView.findViewById(R.id.cancelled_offer_price);
            finalPrice = itemView.findViewById(R.id.final_offer_price);
            offerImage = itemView.findViewById(R.id.offer_image);
            offerDescription = itemView.findViewById(R.id.offer_description);
            bookNow = itemView.findViewById(R.id.book_now);
        }
    }
}


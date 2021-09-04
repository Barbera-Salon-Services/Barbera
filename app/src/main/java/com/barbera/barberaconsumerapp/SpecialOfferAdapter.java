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
        holder.offerDescriptionName.setText(specialOfferData.getOfferDescriptionName());
        holder.offerDescriptionPrice.setText(specialOfferData.getOfferDescriptionPrice());
        holder.offerTotalPrice.setText(specialOfferData.getOfferTotalPrice());
        holder.offerPrice.setText(specialOfferData.getOfferPrice());

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
        TextView offerName, offerDescriptionName, offerDescriptionPrice, offerTotalPrice, offerPrice, bookNow;
        ImageView offerImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            offerName = itemView.findViewById(R.id.offer_name);
            offerDescriptionName = itemView.findViewById(R.id.offer_description_name);
            offerDescriptionPrice = itemView.findViewById(R.id.offer_description_price);
            offerTotalPrice = itemView.findViewById(R.id.total_price);
            offerPrice = itemView.findViewById(R.id.offer_price);
            bookNow = itemView.findViewById(R.id.book_now_text);
            offerImage = itemView.findViewById(R.id.offer_image);

        }
    }
}


package com.barbera.barberaconsumerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.Objects;

public class OfferViewPagerAdapter extends PagerAdapter {
    Context context;
    int[] Offer_images;
    LayoutInflater mLayoutInflater;

    public OfferViewPagerAdapter(Context context, int[] Offer_images) {
        this.context =context;
        this.Offer_images=Offer_images;
        mLayoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return Offer_images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==((LinearLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView= mLayoutInflater.inflate(R.layout.offer_imageview,container,false);
        ImageView imageView = itemView.findViewById(R.id.OfferImageView);
        imageView.setImageResource(Offer_images[position]);
        Objects.requireNonNull(container).addView(itemView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"click listner on slider is working",Toast.LENGTH_SHORT);
            }
        });
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}

package com.barbera.barberaconsumerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeActivityTopImageViewAdapter extends RecyclerView.Adapter<HomeActivityTopImageViewAdapter.ImageViewHolder> {
    Context context;
    int[] different_Section_images;
    LayoutInflater mLayoutInflater;

    public HomeActivityTopImageViewAdapter(Context context, int[] different_Section_images  ){
        this.context=context;
        this.different_Section_images=different_Section_images;
        this.mLayoutInflater=LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= mLayoutInflater.inflate(R.layout.recylerview_item_activityhome_top_imageview,parent,false);
        ImageViewHolder holder = new ImageViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

        holder.differentSectionImageView.setImageResource(different_Section_images[position]);
    }

    @Override
    public int getItemCount() {
        return different_Section_images.length;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        ImageView differentSectionImageView;

        public ImageViewHolder(View itemView){
            super(itemView);
            differentSectionImageView=itemView.findViewById(R.id.different_section_images);
        }
    }

}

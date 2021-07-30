package com.barbera.barberaconsumerapp.Services;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.barbera.barberaconsumerapp.R;
import com.barbera.barberaconsumerapp.Utils.SliderItem;
import com.bumptech.glide.Glide;

import java.util.List;

public class HomeActivityTopImageViewAdapter extends RecyclerView.Adapter<HomeActivityTopImageViewAdapter.ImageViewHolder> {
    private Context context;
    private List<SliderItem> list;
    private LayoutInflater mLayoutInflater;

    public HomeActivityTopImageViewAdapter(Context context, List<SliderItem> list){
        this.context=context;
        this.list=list;
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
        SliderItem item=list.get(position);
        Glide.with(context).load(item.getUrl()).into(holder.differentSectionImageView);
        holder.name.setText(item.getName());
        Log.d("swe",item.getName());
        holder.differentSectionImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder{
        private ImageView differentSectionImageView;
        private TextView name;

        public ImageViewHolder(View itemView){
            super(itemView);
            differentSectionImageView=itemView.findViewById(R.id.different_section_images);
            name=itemView.findViewById(R.id.top_text);
        }
    }

}

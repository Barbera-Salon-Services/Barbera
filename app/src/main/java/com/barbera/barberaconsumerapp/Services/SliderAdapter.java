package com.barbera.barberaconsumerapp.Services;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.barbera.barberaconsumerapp.R;
import com.barbera.barberaconsumerapp.Utils.SliderItem;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {
    private List<SliderItem> list;
    private Context context;

    public SliderAdapter(List<SliderItem> list, Context context) {
        this.list = list;
        this.context=context;
    }

    @NonNull
    @NotNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item,parent,false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SliderViewHolder holder, int position) {
        SliderItem item=list.get(position);
        Log.d("sds",item.getUrl());
        Toast.makeText(context,"Hell "+item.getName(),Toast.LENGTH_SHORT).show();
        Glide.with(context).load(item.getUrl()).into(holder.img);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ServiceType.class);
                intent.putExtra("SalonType",item.getCategory());
                intent.putExtra("Category",item.getTypes());
                intent.putExtra("ImageUrl",item.getUrl());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class SliderViewHolder extends RecyclerView.ViewHolder{
        private ImageView img;

        public SliderViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.slider_image);
        }
    }
}

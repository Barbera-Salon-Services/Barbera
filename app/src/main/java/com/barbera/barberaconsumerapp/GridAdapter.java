package com.barbera.barberaconsumerapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

    private List<String> imgUrl, imgName;
    private LayoutInflater inflater;
    private Context context;


    public GridAdapter(List<String> imgUrl, List<String> imgName, Context ctx) {
        this.imgUrl = imgUrl;
        this.imgName = imgName;
        this.inflater = inflater.from(ctx);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.card_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imgName.setText(imgName.get(position));
//        Glide.with(getContext()).load(gridModel.getImgUrl()).into(img);
        Glide.with(context).load(imgUrl.get(position)).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return imgName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView imgName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.grid_img);
            imgName  = itemView.findViewById(R.id.grid_img_text);

            context = itemView.getContext();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Will define later
                }
            });
        }
    }
}

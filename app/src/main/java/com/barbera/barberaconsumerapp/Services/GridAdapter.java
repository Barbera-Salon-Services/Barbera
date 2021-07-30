package com.barbera.barberaconsumerapp.Services;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.barbera.barberaconsumerapp.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

    private List<String> imgUrl=new ArrayList<>(), imgName=new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;
    private String salonType;


    public GridAdapter(List<String> imgUrl, List<String> imgName, Context ctx,String category) {
        this.imgUrl = imgUrl;
        this.imgName = imgName;
        this.inflater = inflater.from(ctx);
        this.context=ctx;
        this.salonType=category;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.card_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imgNam.setText(imgName.get(position));
//        Glide.with(getContext()).load(gridModel.getImgUrl()).into(img);
        Glide.with(context).load(imgUrl.get(position)).into(holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ServiceType.class);
                intent.putExtra("Category",imgName.get(position));
                intent.putExtra("SalonType",salonType);
                intent.putExtra("ImageUrl",imgUrl.get(position));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imgName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView imgNam;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.grid_img);
            imgNam  = itemView.findViewById(R.id.grid_img_text);
            context = itemView.getContext();
        }
    }
}

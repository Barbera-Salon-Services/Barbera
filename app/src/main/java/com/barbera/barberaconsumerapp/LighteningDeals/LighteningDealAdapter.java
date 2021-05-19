package com.barbera.barberaconsumerapp.LighteningDeals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.barbera.barberaconsumerapp.MenHorizontalAdapter;
import com.barbera.barberaconsumerapp.R;
import com.barbera.barberaconsumerapp.Service;

import java.util.List;

public class LighteningDealAdapter extends RecyclerView.Adapter {
    private List<LightenDealItem> list;
    private Context activity;

    public LighteningDealAdapter(List<LightenDealItem> list, Context activity) {
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.lightening_deal_item,parent,false);
        return new DealItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class DealItemHolder extends RecyclerView.ViewHolder{
        String x;
        public DealItemHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

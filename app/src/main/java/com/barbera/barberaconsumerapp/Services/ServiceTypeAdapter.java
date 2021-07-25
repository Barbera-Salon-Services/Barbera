package com.barbera.barberaconsumerapp.Services;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.barbera.barberaconsumerapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ServiceTypeAdapter extends RecyclerView.Adapter<ServiceTypeAdapter.ServiceViewHolder> {
    private final Context context;
    private final List<ServiceOuterItem> list;

    public ServiceTypeAdapter(Context context, List<ServiceOuterItem> serviceList) {
        this.context=context;
        this.list = serviceList;
    }
    @NotNull
    @Override
    public ServiceTypeAdapter.ServiceViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subtype,parent,false);
        return new ServiceViewHolder(view);
    }
    @NonNull
    @Override
    public void onBindViewHolder(@NonNull ServiceTypeAdapter.ServiceViewHolder holder, int position) {
        //Log.d("siz",""+list.size());
        Log.d("sd",""+list.get(position).getSubtype());
        ServiceOuterItem serviceOuterItem=list.get(position);
        holder.name.setText(serviceOuterItem.getSubtype());

        LinearLayoutManager llm = new LinearLayoutManager(context);
        holder.recyclerView.setLayoutManager(llm);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        ServiceAdapter serviceAdapter=new ServiceAdapter(context,serviceOuterItem.getServiceItemList());
        holder.recyclerView.setAdapter(serviceAdapter);
        holder.arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.recyclerView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder{
        private final TextView name;
        private final Button arrow;
        private final RecyclerView recyclerView;

        public ServiceViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.subtype_name);
            arrow=itemView.findViewById(R.id.dropdown);
            recyclerView=itemView.findViewById(R.id.subservice_recycler_view);

        }
    }
}

package com.barbera.barberaconsumerapp.Services;

import android.content.Context;
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
    private Context context;
    private List<ServiceOuterItem> list=new ArrayList<>();
    private ServiceAdapter serviceAdapter;
    public ServiceTypeAdapter(Context context, List<ServiceOuterItem> serviceList) {
        this.context=context;
        this.list = serviceList;
    }
    @NotNull
    @Override
    public ServiceTypeAdapter.ServiceViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subtype,parent,false);
        return new ServiceTypeAdapter.ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ServiceTypeAdapter.ServiceViewHolder holder, int position) {
        ServiceOuterItem serviceOuterItem=list.get(position);
        holder.name.setText(serviceOuterItem.getSubtype());
        holder.arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayoutManager llm = new LinearLayoutManager(context);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                holder.recyclerView.setVisibility(View.VISIBLE);
                holder.recyclerView.setLayoutManager(llm);
                serviceAdapter=new ServiceAdapter(context,serviceOuterItem.getServiceItemList());
                holder.recyclerView.setAdapter(serviceAdapter);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ServiceViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private Button arrow;
        private RecyclerView recyclerView;

        public ServiceViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.subtype_name);
            arrow=itemView.findViewById(R.id.dropdown);
            recyclerView=itemView.findViewById(R.id.subservice_recycler_view);

        }
    }
}

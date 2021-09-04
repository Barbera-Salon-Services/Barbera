package com.barbera.barberaconsumerapp.Services;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.barbera.barberaconsumerapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ServiceTypeAdapter extends RecyclerView.Adapter<ServiceTypeAdapter.ServiceViewHolder> {
    private final Context context;
    private final List<ServiceOuterItem> list;
    private String salonType;
    private ServiceAdapter serviceAdapter;
    RelativeLayout progressDialogView;

    public ServiceTypeAdapter(Context context, List<ServiceOuterItem> serviceList, String salonType, RelativeLayout progressDialogView) {
        this.context = context;
        this.list = serviceList;
        this.salonType = salonType;
        this.progressDialogView = progressDialogView;
    }

    @NotNull
    @Override
    public ServiceTypeAdapter.ServiceViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subtype, parent, false);
        return new ServiceViewHolder(view);
    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull ServiceTypeAdapter.ServiceViewHolder holder, int position) {
        //Log.d("siz",""+list.size());
        Log.d("sd", "" + list.get(position).getSubtype());
        ServiceOuterItem serviceOuterItem = list.get(position);
        holder.name.setText(serviceOuterItem.getSubtype());

        LinearLayoutManager llm = new LinearLayoutManager(context);
        holder.recyclerView.setLayoutManager(llm);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        serviceAdapter = new ServiceAdapter(context, serviceOuterItem.getServiceItemList(), salonType, progressDialogView);
        holder.recyclerView.setAdapter(serviceAdapter);
        final int[] i = {0};
        holder.arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i[0] == 0) {
                    holder.recyclerView.setVisibility(View.VISIBLE);
                    i[0] = 1;
                } else {
                    i[0] = 0;
                    holder.recyclerView.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final LinearLayout arrow;
        private final RecyclerView recyclerView;

        public ServiceViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.subtype_name);
            arrow = itemView.findViewById(R.id.sub_click);
            recyclerView = itemView.findViewById(R.id.subservice_recycler_view);

        }
    }
}

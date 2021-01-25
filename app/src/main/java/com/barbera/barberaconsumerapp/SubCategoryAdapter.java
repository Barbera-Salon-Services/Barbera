package com.barbera.barberaconsumerapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SubCategoryAdapter extends BaseAdapter {
    private List<String> sublist;

    public SubCategoryAdapter(List<String> sublist) {
        this.sublist = sublist;
    }

    @Override
    public int getCount() {
        return sublist.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view;
        if (convertView == null)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_service_piece, null);
        else
            view = (View) convertView;

        TextView title = view.findViewById(R.id.service_fragement_title);
        TextView price = view.findViewById(R.id.service_fragement_price);
        TextView cutPrice=view.findViewById(R.id.service_fragement_cut_price);
        final CheckBox checkBox=view.findViewById(R.id.service_fragement_check_box);
        TextView time=view.findViewById(R.id.service_fragement_time);
        ImageView timeImage=view.findViewById(R.id.timer);
        View line=view.findViewById(R.id.line);
        ImageView arrow=view.findViewById(R.id.subcategoryArrow);

        price.setVisibility(View.GONE);
        cutPrice.setVisibility(View.GONE);
        checkBox.setVisibility(View.GONE);
        time.setVisibility(View.GONE);
        timeImage.setVisibility(View.GONE);
        line.setVisibility(View.GONE);
        arrow.setVisibility(View.VISIBLE);

        title.setText(sublist.get(position));

        return view;
    }
}

package com.barbera.barberaconsumerapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {

    private List<String> list;

    public CategoryAdapter(List<String> list) {
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getCount() {
        return list.size();
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_design, null);
        else
            view = (View) convertView;

        ImageView img=view.findViewById(R.id.category_image);
        TextView text=view.findViewById(R.id.category_text);

        Glide.with(view.getContext()).load(list.get(position))
                .apply(new RequestOptions().placeholder(R.drawable.logo)).into(img);
        text.setText(list.get(position));
        return view;
    }
}

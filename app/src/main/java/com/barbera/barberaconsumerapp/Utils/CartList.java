package com.barbera.barberaconsumerapp.Utils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CartList {
    @SerializedName("data")
    private List<CartItemModel> list;

    @SerializedName("count")
    private int count;

    public CartList(List<CartItemModel> list,int count) {
        this.list = list;
        this.count=count;
    }

    public int getCount() {
        return count;
    }

    public List<CartItemModel> getList() {
        return list;
    }
}

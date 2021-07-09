package com.barbera.barberaconsumerapp.Utils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CartList2 {
    @SerializedName("service")
    private List<CartItemModel> list;

    public CartList2(List<CartItemModel> list) {
        this.list = list;
    }

    public List<CartItemModel> getList() {
        return list;
    }
}

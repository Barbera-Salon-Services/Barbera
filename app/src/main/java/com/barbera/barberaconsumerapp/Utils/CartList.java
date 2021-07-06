package com.barbera.barberaconsumerapp.Utils;

import com.barbera.barberaconsumerapp.CartItemModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CartList {
    @SerializedName("data")
    private List<CartItemModel> list;

    public CartList(List<CartItemModel> list) {
        this.list = list;
    }

    public List<CartItemModel> getList() {
        return list;
    }
}

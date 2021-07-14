package com.barbera.barberaconsumerapp.Bookings;

import com.barbera.barberaconsumerapp.Utils.CartItemModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServiceIdList {
    @SerializedName("service")
    private List<CartItemModel> list;
    @SerializedName("barberid")
    private String id;

    public ServiceIdList(List<CartItemModel> list, String id) {
        this.list = list;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public List<CartItemModel> getList() {
        return list;
    }
}

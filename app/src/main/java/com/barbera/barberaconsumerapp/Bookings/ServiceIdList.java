package com.barbera.barberaconsumerapp.Bookings;

import com.barbera.barberaconsumerapp.Utils.CartItemModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServiceIdList {
    @SerializedName("service")
    private List<CartItemModel> list;
    @SerializedName("barberId")
    private String id;
    @SerializedName("slot")
    private String slot;
    @SerializedName("totalprice")
    private int totalPrice;

    public ServiceIdList(List<CartItemModel> list, String id,String slot) {
        this.list = list;
        this.id = id;
        this.slot=slot;
    }

    public String getSlot() {
        return slot;
    }

    public String getId() {
        return id;
    }

    public List<CartItemModel> getList() {
        return list;
    }
}

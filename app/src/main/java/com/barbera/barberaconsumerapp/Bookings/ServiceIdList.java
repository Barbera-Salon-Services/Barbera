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
    @SerializedName("couponName")
    private String couponName;


    public ServiceIdList(List<CartItemModel> list, String id,String slot,int totalPrice,String couponName) {
        this.list = list;
        this.id = id;
        this.slot=slot;
        this.totalPrice=totalPrice;
        this.couponName=couponName;
    }

    public String getCouponName() {
        return couponName;
    }

    public int getTotalPrice() {
        return totalPrice;
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

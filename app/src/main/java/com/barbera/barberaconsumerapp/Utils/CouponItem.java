package com.barbera.barberaconsumerapp.Utils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CouponItem {
    @SerializedName("serviceId")
    private String serviceId;
    @SerializedName("upper_price_limit")
    private int upperLimit;
    @SerializedName("lower_price_limit")
    private int lowerLimit;
    @SerializedName("data")
    private int discount;

    public CouponItem(String serviceId, int upperLimit, int lowerLimit,int discount) {
        this.serviceId = serviceId;
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
        this.discount=discount;
    }

    public int getDiscount() {
        return discount;
    }

    public int getLowerLimit() {
        return lowerLimit;
    }

    public int getUpperLimit() {
        return upperLimit;
    }

    public String getServiceId() {
        return serviceId;
    }
}

package com.barbera.barberaconsumerapp.Utils;

import com.google.gson.annotations.SerializedName;

public class GetCouponItem {
    @SerializedName("serviceId")
    private String serviceId;
    @SerializedName("name")
    private String name;
    @SerializedName("discount")
    private int discount;
    @SerializedName("upper_price_limit")
    private int upperLimit;
    @SerializedName("lower_price_limit")
    private int lowerLimit;
    @SerializedName("terms")
    private String terms;

    public GetCouponItem(String serviceId, String name, int discount, int upperLimit, int lowerLimit,String terms) {
        this.serviceId = serviceId;
        this.name = name;
        this.discount = discount;
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
        this.terms=terms;
    }

    public String getTerms() {
        return terms;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getName() {
        return name;
    }

    public int getDiscount() {
        return discount;
    }

    public int getUpperLimit() {
        return upperLimit;
    }

    public int getLowerLimit() {
        return lowerLimit;
    }
}

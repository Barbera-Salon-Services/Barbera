package com.barbera.barberaconsumerapp.Utils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Offer {
    @SerializedName("serviceId")
    private String serviceId;
    @SerializedName("name")
    private String name;
    @SerializedName("discount")
    private int discount;
    @SerializedName("user_limit")
    private int user_limit;
    @SerializedName("terms")
    private String terms;
    @SerializedName("image")
    private String image;
    @SerializedName("start")
    private String start;
    @SerializedName("end")
    private String end;
    @SerializedName("service")
    private ServiceItem serviceItem;

    public Offer(String serviceId, String name, int discount, int user_limit, String terms, String image, String start, String end,ServiceItem serviceItem) {
        this.serviceId = serviceId;
        this.name = name;
        this.discount = discount;
        this.user_limit = user_limit;
        this.terms = terms;
        this.image = image;
        this.start = start;
        this.end = end;
        this.serviceItem=serviceItem;
    }

    public ServiceItem getServiceItem() {
        return serviceItem;
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

    public int getUser_limit() {
        return user_limit;
    }

    public String getTerms() {
        return terms;
    }

    public String getImage() {
        return image;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }
}

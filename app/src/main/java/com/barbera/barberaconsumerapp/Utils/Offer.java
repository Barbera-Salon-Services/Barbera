package com.barbera.barberaconsumerapp.Utils;

import com.google.gson.annotations.SerializedName;

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

    public Offer(String serviceId, String name, int discount, int user_limit, String terms, String image, String start, String end) {
        this.serviceId = serviceId;
        this.name = name;
        this.discount = discount;
        this.user_limit = user_limit;
        this.terms = terms;
        this.image = image;
        this.start = start;
        this.end = end;
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

package com.barbera.barberaconsumerapp.Utils;

import com.google.gson.annotations.SerializedName;

public class ServiceItem {
    @SerializedName("name")
    private String name;
    @SerializedName("price")
    private String price;
    @SerializedName("time")
    private String time;
    @SerializedName("details")
    private String detail;
    @SerializedName("cutprice")
    private String cutprice;
    @SerializedName("gender")
    private String gender;
    @SerializedName("type")
    private String type;
    @SerializedName("subtype")
    private String subtype;
    @SerializedName("dod")
    private boolean dod;
    @SerializedName("trending")
    private boolean trend;
    @SerializedName("id")
    private String id;


    public ServiceItem(String name, String price, String time, String detail, String cutprice,
                       String gender, String type, boolean dod, String id, boolean trend,String subtype) {
        this.name = name;
        this.price = price;
        this.time = time;
        this.detail = detail;
        this.cutprice = cutprice;
        this.gender = gender;
        this.type = type;
        this.dod = dod;
        this.id=id;
        this.trend=trend;
        this.subtype=subtype;
    }

    public String getSubtype() {
        return subtype;
    }

    public boolean isTrend() {
        return trend;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getTime() {
        return time;
    }

    public String getDetail() {
        return detail;
    }

    public String getCutprice() {
        return cutprice;
    }

    public String getGender() {
        return gender;
    }

    public String getType() {
        return type;
    }

    public boolean isDod() {
        return dod;
    }
}

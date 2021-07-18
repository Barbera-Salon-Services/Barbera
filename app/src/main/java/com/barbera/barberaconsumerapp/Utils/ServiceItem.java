package com.barbera.barberaconsumerapp.Utils;

import com.google.gson.annotations.SerializedName;

public class ServiceItem {
    @SerializedName("name")
    private String name;
    @SerializedName("price")
    private int price;
    @SerializedName("image")
    private String image;
    @SerializedName("time")
    private int time;
    @SerializedName("details")
    private String detail;
    @SerializedName("cutprice")
    private int cutprice;
    @SerializedName("category")
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


    public ServiceItem(String name, int price, int time, String detail, int cutprice, String gender,
                       String type, boolean dod, String id, boolean trend,String subtype,String image) {
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
        this.image=image;
    }

    public String getImage() {
        return image;
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

    public int getPrice() {
        return price;
    }

    public int getTime() {
        return time;
    }

    public String getDetail() {
        return detail;
    }

    public int getCutprice() {
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

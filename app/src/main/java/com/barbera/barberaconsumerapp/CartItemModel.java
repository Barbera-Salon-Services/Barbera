package com.barbera.barberaconsumerapp;

import com.google.gson.annotations.SerializedName;

public class CartItemModel {
    private String ImageId;
    @SerializedName("name")
    private String ServiceName;
    @SerializedName("price")
    private String ServicePrice;
    @SerializedName("quantity")
    private int Quantity;
    private String type;
    private String serviceId;
    private int index;
    @SerializedName("time")
    private String Time;

    public CartItemModel(String imageId, String serviceName, String servicePrice, String type,String ServiceId,int Index, String time) {
        ImageId = imageId;
        ServiceName = serviceName;
        ServicePrice =  servicePrice;
        this.type = type;
        serviceId=ServiceId;
        index=Index;
        Quantity=1;
        Time=time;
    }

    public String getTime() {
        return Time;
    }

    public int getIndex() {
        return index;
    }

    public String getImageId() {
        return ImageId;
    }

    public String getType() {
        return type;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getServicePrice() {
        return ServicePrice;
    }

    public int getQuantity() {
        return Quantity;
    }
}

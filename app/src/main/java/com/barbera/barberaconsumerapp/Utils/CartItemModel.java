package com.barbera.barberaconsumerapp.Utils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import javax.xml.namespace.QName;

public class CartItemModel implements Serializable {
    private String ImageId;
    @SerializedName("name")
    private String ServiceName;
    @SerializedName("price")
    private int ServicePrice;   //price*quantity - discount
    @SerializedName("quantity")
    private int Quantity;
    private String type;
    @SerializedName("time")
    private int Time;
    @SerializedName("serviceId")
    private String id;
    @SerializedName("flow")
    private boolean flow;
    @SerializedName("totalprice")
    private int totalPrice;

    public CartItemModel(String imageId, String serviceName, int servicePrice, String type,
                         int quantity, int time,String id,boolean flow,int totalPrice) {
        ImageId = imageId;
        ServiceName = serviceName;
        ServicePrice =  servicePrice;
        this.type = type;
        this.Quantity= quantity;
        Time=time;
        this.id=id;
        this.flow=flow;
        this.totalPrice=totalPrice;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public boolean isFlow() {
        return flow;
    }

    public String getId() {
        return id;
    }

    public int getTime() {
        return Time;
    }

    public String getImageId() {
        return ImageId;
    }

    public String getType() {
        return type;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public int getServicePrice() {
        return ServicePrice;
    }

    public int getQuantity() {
        return Quantity;
    }
}

package com.barbera.barberaconsumerapp.Utils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import javax.xml.namespace.QName;

public class CartItemModel implements Serializable {
    private String ImageId;
    @SerializedName("name")
    private String ServiceName;
    @SerializedName("price")
    private int ServicePrice;
    @SerializedName("quantity")
    private int Quantity;
    @SerializedName("category")
    private String category;
    @SerializedName("type")
    private String type;
    @SerializedName("time")
    private int Time;
    @SerializedName("serviceId")
    private String id;
    @SerializedName("flow")
    private boolean flow;
    @SerializedName("offerName")
    private String offerName;

    public CartItemModel(String imageId, String serviceName, int servicePrice, String category,
                         int quantity, int time,String id,boolean flow,String type,String offerName) {
        ImageId = imageId;
        ServiceName = serviceName;
        ServicePrice =  servicePrice;
        this.category = category;
        this.Quantity= quantity;
        Time=time;
        this.id=id;
        this.flow=flow;
        this.type=type;
        this.offerName=offerName;
    }

    public String getOfferName() {
        return offerName;
    }

    public String getCategory() {
        return category;
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

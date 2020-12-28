package com.barbera.barberaconsumerapp;

public class CartItemModel {
    private String ImageId;
    private String ServiceName;
    private String ServicePrice;
    private int Quantity;
    private String type;
    private String serviceId;
    private int index;

    public CartItemModel(String imageId, String serviceName, String servicePrice, String type,String ServiceId,int Index) {
        ImageId = imageId;
        ServiceName = serviceName;
        ServicePrice =  servicePrice;
        this.type = type;
        serviceId=ServiceId;
        index=Index;
        Quantity=1;
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

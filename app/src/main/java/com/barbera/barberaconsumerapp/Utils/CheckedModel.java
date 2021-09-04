package com.barbera.barberaconsumerapp.Utils;

public class CheckedModel {
    private String serviceId;
    private int servicePrice;
    private String serviceName;
    private int serviceTime;

    public CheckedModel(String serviceId,String serviceName ,int servicePrice,int serviceTime) {
        this.serviceName=serviceName;
        this.serviceId = serviceId;
        this.servicePrice = servicePrice;
        this.serviceTime = serviceTime;
    }

    public int getTime() {
        return serviceTime;
    }

    public String getId() {
        return serviceId;
    }

    public int getPrice() {
        return servicePrice;
    }

    public String getName() {
        return serviceName;
    }
}

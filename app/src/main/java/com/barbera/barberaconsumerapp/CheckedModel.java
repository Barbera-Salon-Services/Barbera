package com.barbera.barberaconsumerapp;

public class CheckedModel {
    private String serviceId;
    private String servicePrice;
    private String serviceName;
    private String serviceTime;

    public CheckedModel(String serviceId,String serviceName ,String servicePrice,String serviceTime) {
        this.serviceName=serviceName;
        this.serviceId = serviceId;
        this.servicePrice = servicePrice;
        this.serviceTime = serviceTime;
    }

    public String getTime() {
        return serviceTime;
    }

    public String getId() {
        return serviceId;
    }

    public String getPrice() {
        return servicePrice;
    }

    public String getName() {
        return serviceName;
    }
}

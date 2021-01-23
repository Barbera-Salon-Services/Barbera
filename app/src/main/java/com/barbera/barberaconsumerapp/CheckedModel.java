package com.barbera.barberaconsumerapp;

public class CheckedModel {
    private String serviceId;
    private String servicePrice;
    private String serviceName;

    public CheckedModel(String serviceId,String serviceName ,String servicePrice) {
        this.serviceName=serviceName;
        this.serviceId = serviceId;
        this.servicePrice = servicePrice;
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

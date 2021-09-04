package com.barbera.barberaconsumerapp.Utils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServiceList {
    @SerializedName("data")
    private List<ServiceItem> serviceItems;

    public ServiceList(List<ServiceItem> serviceItems) {
        this.serviceItems = serviceItems;
    }

    public List<ServiceItem> getServices() {
        return serviceItems;
    }
}

package com.barbera.barberaconsumerapp.Services;

import com.barbera.barberaconsumerapp.Utils.ServiceItem;

import java.util.List;

public class ServiceOuterItem {
    private String subtype;
    private List<ServiceItem> serviceItemList;

    public ServiceOuterItem(String subtype, List<ServiceItem> serviceItemList) {
        this.subtype = subtype;
        this.serviceItemList = serviceItemList;
    }

    public String getSubtype() {
        return subtype;
    }

    public List<ServiceItem> getServiceItemList() {
        return serviceItemList;
    }
}

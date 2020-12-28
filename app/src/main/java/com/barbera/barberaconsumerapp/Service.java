package com.barbera.barberaconsumerapp;

public class Service {
    private String ImageId;
    private String ServiceName;
    private String Price;
    private String serviceId;
    private String serviceType;
    private String cutPrice;

    public Service(String imageId, String serviceName, String price, String serviceId, String serviceType,String cutPrice) {
        ImageId = imageId;
        ServiceName = serviceName;
        Price = price;
        this.serviceId = serviceId;
        this.serviceType = serviceType;
        this.cutPrice=cutPrice;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getCutPrice() {
        return cutPrice;
    }

    public String getImageId() {
        return ImageId;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public String getPrice() {
        return Price;
    }

}

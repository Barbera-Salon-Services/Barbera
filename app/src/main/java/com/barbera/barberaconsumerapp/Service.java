package com.barbera.barberaconsumerapp;

public class Service {
    private String ImageId;
    private String ServiceName;
    private String Price;
    private String serviceId;
    private String serviceType;
    private String cutPrice;
    private String time;
    private String details;

    public Service(String imageId, String serviceName, String price, String serviceId, String serviceType,String cutPrice,String itime,String details) {
        ImageId = imageId;
        ServiceName = serviceName;
        Price = price;
        this.serviceId = serviceId;
        this.serviceType = serviceType;
        this.cutPrice=cutPrice;
        this.time=itime;
        this.details=details;
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

    public String getTime() {
        return time;
    }

    public String getDetails() {return details;}
}

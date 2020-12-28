package com.barbera.barberaconsumerapp;

public class CouponItemModel {
    private String ImageId;
    private String ServiceName;
    private String ServicePrice;
    private String type;
    private int index;

    public CouponItemModel(String imageId, String serviceName, String servicePrice, String type, int index) {
        ImageId = imageId;
        ServiceName = serviceName;
        ServicePrice = servicePrice;
        this.type = type;
        this.index = index;
    }

    public String getImageId() {
        return ImageId;
    }

    public void setImageId(String imageId) {
        ImageId = imageId;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }

    public String getServicePrice() {
        return ServicePrice;
    }

    public void setServicePrice(String servicePrice) {
        ServicePrice = servicePrice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}

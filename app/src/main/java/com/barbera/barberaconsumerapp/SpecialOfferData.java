package com.barbera.barberaconsumerapp;

public class SpecialOfferData {
    private String offerName;
    private String time;
    private String cancelledPrice;
    private String finalPrice;
    private String offerDescription;
    private String offerImageUrl;

    public SpecialOfferData(String offerName, String time, String cancelledPrice, String finalPrice, String offerDescription, String offerImageUrl) {
        this.offerName = offerName;
        this.time = time;
        this.cancelledPrice = cancelledPrice;
        this.finalPrice = finalPrice;
        this.offerDescription = offerDescription;
        this.offerImageUrl = offerImageUrl;
    }

    public String getOfferDescription() {
        return offerDescription;
    }

    public void setOfferDescription(String offerDescription) {
        this.offerDescription = offerDescription;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCancelledPrice() {
        return cancelledPrice;
    }

    public void setCancelledPrice(String cancelledPrice) {
        this.cancelledPrice = cancelledPrice;
    }

    public String getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(String finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getOfferImageUrl() {
        return offerImageUrl;
    }

    public void setOfferImageUrl(String offerImageUrl) {
        this.offerImageUrl = offerImageUrl;
    }
}

package com.barbera.barberaconsumerapp;

public class SpecialOfferData {
    private String offerName;
    private String offerDescriptionName;
    private String offerDescriptionPrice;
    private String offerTotalPrice;
    private String offerPrice;
    private String offerImageUrl;

    public SpecialOfferData(String offerName, String offerDescriptionName, String offerDescriptionPrice, String offerTotalPrice, String offerPrice, String offerImageUrl) {
        this.offerName = offerName;
        this.offerDescriptionName = offerDescriptionName;
        this.offerDescriptionPrice = offerDescriptionPrice;
        this.offerTotalPrice = offerTotalPrice;
        this.offerPrice = offerPrice;
        this.offerImageUrl = offerImageUrl;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getOfferDescriptionName() {
        return offerDescriptionName;
    }

    public void setOfferDescriptionName(String offerDescriptionName) {
        this.offerDescriptionName = offerDescriptionName;
    }

    public String getOfferDescriptionPrice() {
        return offerDescriptionPrice;
    }

    public void setOfferDescriptionPrice(String offerDescriptionPrice) {
        this.offerDescriptionPrice = offerDescriptionPrice;
    }

    public String getOfferTotalPrice() {
        return offerTotalPrice;
    }

    public void setOfferTotalPrice(String offerTotalPrice) {
        this.offerTotalPrice = offerTotalPrice;
    }

    public String getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(String offerPrice) {
        this.offerPrice = offerPrice;
    }

    public String getOfferImageUrl() {
        return offerImageUrl;
    }

    public void setOfferImageUrl(String offerImageUrl) {
        this.offerImageUrl = offerImageUrl;
    }
}

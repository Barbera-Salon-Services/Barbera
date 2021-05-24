package com.barbera.barberaconsumerapp.LighteningDeals;

public class LightenDealItem {
    private  String title;
    private String image_url;
    private int time;
    private int price;
    private int discount;

    public LightenDealItem(String title, String image_url, int time, int price, int discount) {
        this.title = title;
        this.image_url = image_url;
        this.time = time;
        this.price = price;
        this.discount = discount;
    }

    public String getTitle() {
        return title;
    }

    public String getImage_url() {
        return image_url;
    }

    public int getTime() {
        return time;
    }

    public int getPrice() {
        return price;
    }

    public int getDiscount() {
        return discount;
    }
}

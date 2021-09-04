package com.barbera.barberaconsumerapp.Bookings;

import com.google.gson.annotations.SerializedName;

public class BarberItem {
    @SerializedName("id")
    private String barberid;
    @SerializedName("address")
    private String address;
    @SerializedName("phone")
    private String phone;
    @SerializedName("distance")
    private double distance;
    @SerializedName("name")
    private String name;

    public BarberItem(String barberid, String address, String phone, double distance,String name) {
        this.barberid = barberid;
        this.address = address;
        this.phone = phone;
        this.distance=distance;
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public double getDistance() {
        return distance;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getBarberid() {
        return barberid;
    }
}

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
    private String distance;

    public BarberItem(String barberid, String address, String phone, String distance) {
        this.barberid = barberid;
        this.address = address;
        this.phone = phone;
        this.distance=distance;
    }

    public String getDistance() {
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

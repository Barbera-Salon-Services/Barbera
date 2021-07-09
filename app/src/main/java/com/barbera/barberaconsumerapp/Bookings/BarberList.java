package com.barbera.barberaconsumerapp.Bookings;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BarberList {
    @SerializedName("data")
    private List<BarberItem> list;

    public BarberList(List<BarberItem> list) {
        this.list = list;
    }

    public List<BarberItem> getList() {
        return list;
    }
}

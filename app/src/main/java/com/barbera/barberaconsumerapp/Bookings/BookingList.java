package com.barbera.barberaconsumerapp.Bookings;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookingList {
    @SerializedName("done")
    List<BookingItem> list;

    public BookingList(List<BookingItem> list) {
        this.list = list;
    }

    public List<BookingItem> getList() {
        return list;
    }
}

package com.barbera.barberaconsumerapp.Bookings;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookedList {
    @SerializedName("data")
    private BookedItem list;

    public BookedList(BookedItem list) {
        this.list = list;
    }

    public BookedItem getList() {
        return list;
    }
}

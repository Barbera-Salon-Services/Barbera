package com.barbera.barberaconsumerapp.Bookings;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookedList {
    @SerializedName("data")
    private BookedItem list;
    @SerializedName("gender")
    private String gender;

    public BookedList(BookedItem list, String gender) {
        this.list = list;
        this.gender=gender;
    }
    public String getGender() {
        return gender;
    }

    public BookedItem getList() {
        return list;
    }
}

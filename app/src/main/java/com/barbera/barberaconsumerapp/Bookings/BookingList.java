package com.barbera.barberaconsumerapp.Bookings;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookingList {
    @SerializedName("done")
    List<BookingItem> list;
    @SerializedName("not_done")
    List<BookingItem> list1;

    public BookingList(List<BookingItem> list,List<BookingItem> list1) {
        this.list = list;
        this.list1=list1;
    }

    public List<BookingItem> getList1() {
        return list1;
    }

    public List<BookingItem> getList() {
        return list;
    }
}

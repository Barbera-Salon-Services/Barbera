package com.barbera.barberaconsumerapp.Bookings;

import com.google.gson.annotations.SerializedName;

public class BookedItem {
    @SerializedName("0")
    private DayItem day1;
    @SerializedName("1")
    private DayItem day2;
    @SerializedName("2")
    private DayItem day3;
    @SerializedName("3")
    private DayItem day4;
    @SerializedName("4")
    private DayItem day5;
    @SerializedName("5")
    private DayItem day6;
    @SerializedName("6")
    private DayItem day7;

    public BookedItem(DayItem day1, DayItem day2, DayItem day3, DayItem day4, DayItem day5, DayItem day6, DayItem day7) {
        this.day1 = day1;
        this.day2 = day2;
        this.day3 = day3;
        this.day4 = day4;
        this.day5 = day5;
        this.day6 = day6;
        this.day7 = day7;
    }

    public DayItem getDay1() {
        return day1;
    }

    public DayItem getDay2() {
        return day2;
    }

    public DayItem getDay3() {
        return day3;
    }

    public DayItem getDay4() {
        return day4;
    }

    public DayItem getDay5() {
        return day5;
    }

    public DayItem getDay6() {
        return day6;
    }

    public DayItem getDay7() {
        return day7;
    }
}

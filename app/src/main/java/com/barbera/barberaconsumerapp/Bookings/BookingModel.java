package com.barbera.barberaconsumerapp.Bookings;

public class BookingModel {

    private String summary;
    private int amount;
    private String date;
    private String time;

    public BookingModel(String summary, int amount, String date,String time) {
        this.summary = summary;
        this.amount = amount;
        this.date = date;
        this.time=time;

    }

    public String getTime() {
        return time;
    }

    public String getSummary() {
        return summary;
    }

    public int getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

}

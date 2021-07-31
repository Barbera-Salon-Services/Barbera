package com.barbera.barberaconsumerapp.Bookings;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookingModel {

    private String summary;
    private int amount;
    @SerializedName("date")
    private String date;
    @SerializedName("slot")
    private String time;
    @SerializedName("barberId")
    private String barberId;
    @SerializedName("serviceId")
    private List<String> serviceIdList;
    private String status;
    private String barberName;
    private String barberPhone;
    private double barberDist;

    public BookingModel(String summary, int amount, String date,String time,String barberId,
                        List<String> serviceIdList,String status,String barberName,String barberPhone,double barberDist) {
        this.summary = summary;
        this.amount = amount;
        this.date = date;
        this.time=time;
        this.barberId=barberId;
        this.serviceIdList=serviceIdList;
        this.status=status;
        this.barberName=barberName;
        this.barberPhone=barberPhone;
        this.barberDist=barberDist;
    }

    public double getBarberDist() {
        return barberDist;
    }

    public String getBarberPhone() {
        return barberPhone;
    }

    public String getBarberName() {
        return barberName;
    }

    public String getStatus() {
        return status;
    }

    public List<String> getServiceIdList() {
        return serviceIdList;
    }

    public String getBarberId() {
        return barberId;
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

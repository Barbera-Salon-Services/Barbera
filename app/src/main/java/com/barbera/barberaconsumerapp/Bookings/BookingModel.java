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
    private String category;
    private String type;
    private String startOtp;
    private String endOtp;

    public BookingModel(String summary, int amount, String date,String time,String barberId,
                        List<String> serviceIdList,String status,String barberName,String barberPhone,double barberDist,
                        String category,String type,String startOtp,String endOtp) {
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
        this.category=category;
        this.type=type;
        this.startOtp=startOtp;
        this.endOtp=endOtp;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEndOtp() {
        return endOtp;
    }

    public String getStartOtp() {
        return startOtp;
    }

    public void setEndOtp(String endOtp) {
        this.endOtp = endOtp;
    }

    public void setStartOtp(String startOtp) {
        this.startOtp = startOtp;
    }

    public String getCategory() {
        return category;
    }

    public String getType() {
        return type;
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

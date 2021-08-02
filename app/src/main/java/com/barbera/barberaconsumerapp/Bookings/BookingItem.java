package com.barbera.barberaconsumerapp.Bookings;

import com.barbera.barberaconsumerapp.Service;
import com.barbera.barberaconsumerapp.Utils.ServiceItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookingItem {
    @SerializedName("Timestamp")
    private String timestamp;
    @SerializedName("date")
    private String date;
    @SerializedName("slot")
    private String slot;
    @SerializedName("service")
    private ServiceItem service;
    @SerializedName("barber")
    private BarberItem barberItem;
    @SerializedName("quantity")
    private int quantity;
    @SerializedName("service_status")
    private String status;
    @SerializedName("serviceId")
    private String serviceId;
    @SerializedName("total_price")
    private int totalPrice;

    public BookingItem(String date, String slot, ServiceItem service, BarberItem barberItem,String timestamp,int quantity,String status,String serviceId,int totalPrice) {
        this.date = date;
        this.slot = slot;
        this.service = service;
        this.barberItem = barberItem;
        this.timestamp=timestamp;
        this.quantity=quantity;
        this.status=status;
        this.serviceId=serviceId;
        this.totalPrice=totalPrice;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getStatus() {
        return status;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getDate() {
        return date;
    }

    public String getSlot() {
        return slot;
    }

    public BarberItem getBarberItem() {
        return barberItem;
    }

    public ServiceItem getService() {
        return service;
    }
}

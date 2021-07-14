package com.barbera.barberaconsumerapp.Bookings;

import com.barbera.barberaconsumerapp.Service;
import com.barbera.barberaconsumerapp.Utils.ServiceItem;
import com.google.gson.annotations.SerializedName;

public class BookingItem {
    @SerializedName("date")
    private String date;
    @SerializedName("slot")
    private String slot;
    @SerializedName("service")
    private ServiceItem service;
    @SerializedName("barber")
    private BarberItem barberItem;

    public BookingItem(String date, String slot, ServiceItem service, BarberItem barberItem) {
        this.date = date;
        this.slot = slot;
        this.service = service;
        this.barberItem = barberItem;
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

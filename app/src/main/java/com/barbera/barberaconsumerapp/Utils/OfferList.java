package com.barbera.barberaconsumerapp.Utils;

import androidx.core.app.NotificationCompat;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OfferList {
    @SerializedName("data")
    private List<Offer> list;

    public OfferList(List<Offer> list) {
        this.list = list;
    }

    public List<Offer> getList() {
        return list;
    }
}

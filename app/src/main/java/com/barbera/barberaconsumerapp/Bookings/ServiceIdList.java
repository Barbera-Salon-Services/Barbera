package com.barbera.barberaconsumerapp.Bookings;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServiceIdList {
    @SerializedName("serviceid")
    private List<String> list;
    @SerializedName("barberid")
    private String id;

    public ServiceIdList(List<String> list, String id) {
        this.list = list;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public List<String> getList() {
        return list;
    }
}

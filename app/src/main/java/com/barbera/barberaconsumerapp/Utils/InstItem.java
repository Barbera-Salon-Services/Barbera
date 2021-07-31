package com.barbera.barberaconsumerapp.Utils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InstItem {
    @SerializedName("barberId")
    private String id;
    @SerializedName("time")
    private int time;
    @SerializedName("slot")
    private String slot;
    @SerializedName("success")
    private boolean success;
    @SerializedName("serviceId")
    private List<String> serviceIdList;

    public  InstItem(String id, int time, String slot,boolean success,List<String> serviceIdList) {
        this.id = id;
        this.time = time;
        this.slot = slot;
        this.success=success;
        this.serviceIdList=serviceIdList;
    }

    public List<String> getServiceIdList() {
        return serviceIdList;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getSlot() {
        return slot;
    }

    public String getId() {
        return id;
    }

    public int getTime() {
        return time;
    }
}

package com.barbera.barberaconsumerapp.Utils;

import com.google.gson.annotations.SerializedName;

public class InstItem {
    @SerializedName("barberId")
    private String id;
    @SerializedName("time")
    private int time;
    @SerializedName("slot")
    private String slot;
    @SerializedName("success")
    private boolean success;

    public InstItem(String id, int time, String slot,boolean success) {
        this.id = id;
        this.time = time;
        this.slot = slot;
        this.success=success;
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

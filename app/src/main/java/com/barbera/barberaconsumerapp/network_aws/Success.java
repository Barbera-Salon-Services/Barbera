package com.barbera.barberaconsumerapp.network_aws;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Success {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<String> list;

    public Success(boolean success, String message,List<String> list) {
        this.success = success;
        this.message = message;
        this.list=list;
    }

    public List<String> getList() {
        return list;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}

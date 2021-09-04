package com.barbera.barberaconsumerapp.network_aws;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SuccessReturn {
    @SerializedName("success")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<String> list;

    @SerializedName("count")
    private int count;

    public SuccessReturn(boolean success, String message,List<String> list,int count) {
        this.success = success;
        this.message = message;
        this.list=list;
        this.count=count;
    }

    public int getCount() {
        return count;
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

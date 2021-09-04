package com.barbera.barberaconsumerapp.network_aws;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Success {
    @SerializedName("serviceid")
    List<String> list;

    public Success(List<String> list) {
        this.list = list;
    }

    public List<String> getList() {
        return list;
    }
}

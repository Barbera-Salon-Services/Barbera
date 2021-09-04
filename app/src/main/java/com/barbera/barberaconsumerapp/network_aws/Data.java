package com.barbera.barberaconsumerapp.network_aws;

import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("data")
    private String ref;
    @SerializedName("success")
    private boolean success;

    public Data(String ref,boolean success) {
        this.ref = ref;
        this.success=success;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getRef() {
        return ref;
    }
}

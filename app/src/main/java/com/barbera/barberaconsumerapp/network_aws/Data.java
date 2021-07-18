package com.barbera.barberaconsumerapp.network_aws;

import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("data")
    private String ref;

    public Data(String ref) {
        this.ref = ref;
    }

    public String getRef() {
        return ref;
    }
}

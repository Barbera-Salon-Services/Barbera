package com.barbera.barberaconsumerapp.network;

import com.google.gson.annotations.SerializedName;

public class Emailer {
    @SerializedName("email")
    private String email;
    @SerializedName("summary")
    private String summary;

    public Emailer(String email, String summary) {
        this.email = email;
        this.summary = summary;
    }

    public String getEmail() {
        return email;
    }

    public String getSummary() {
        return summary;
    }
}

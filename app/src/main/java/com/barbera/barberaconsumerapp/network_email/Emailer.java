package com.barbera.barberaconsumerapp.network_email;

import com.google.gson.annotations.SerializedName;

public class Emailer {
    @SerializedName("email")
    private String email;
    @SerializedName("summary")
    private String summary;
    @SerializedName("datTime")
    private String dateTime;
    @SerializedName("amount")
    private String amount;

    public Emailer(String email, String summary,String dateTime,String amount) {
        this.email = email;
        this.summary = summary;
        this.dateTime  = dateTime;
        this.amount = amount;
    }

    public String getEmail() {
        return email;
    }

    public String getSummary() {
        return summary;
    }
    public String getDateTime(){
        return dateTime;
    }
    public String getAmountt(){
        return amount;

    }
}

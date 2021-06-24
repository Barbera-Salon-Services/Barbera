package com.barbera.barberaconsumerapp.network_aws;

import com.google.gson.annotations.SerializedName;

public class Register {
    @SerializedName("phone")
    private String phone;

    @SerializedName("message")
    private String message;

    @SerializedName("token")
    private String token;

    @SerializedName("otp")
    private String otp;

    @SerializedName("email")
    private String email;

    @SerializedName("name")
    private String name;

    @SerializedName("password")
    private String password;

    @SerializedName("address")
    private String address;

    @SerializedName("role")
    private String role;

    public Register(String phone, String otp, String email, String name, String password, String address, String role,String message) {
        this.phone = phone;
        this.otp = otp;
        this.email = email;
        this.name = name;
        this.password = password;
        this.address = address;
        this.role = role;
        this.message=message;
    }

    public String getMessage() {
        return message;
    }

    public String getAddress() {
        return address;
    }

    public String getRole() {
        return role;
    }

    public String getPhone() {
        return phone;
    }

    public String getToken() {
        return token;
    }

    public String getOtp() {
        return otp;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}

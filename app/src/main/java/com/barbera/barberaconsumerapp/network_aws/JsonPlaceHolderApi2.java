package com.barbera.barberaconsumerapp.network_aws;

import com.barbera.barberaconsumerapp.R;
import com.barbera.barberaconsumerapp.Service;
import com.barbera.barberaconsumerapp.Utils.ServiceList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface JsonPlaceHolderApi2 {
    @POST("loginphone")
    Call<Register> getToken(@Body Register register);

    @POST("loginotp")
    Call<Register> checkOtp(@Body Register register, @Header("token") String token);

    @POST("register")
    Call<Register> updateProfile(@Body Register register, @Header("token") String token);

    @GET("getuser")
    Call<Register> getProfile(@Header("token") String token);

    @GET("gettrend")
    Call<ServiceList> getTrending(@Header("token") String token);


}

package com.barbera.barberaconsumerapp.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface JsonPlaceHolderApi {
    @POST("send")
    Call<Emailer> sendEmail(@Body Emailer emailer);
}

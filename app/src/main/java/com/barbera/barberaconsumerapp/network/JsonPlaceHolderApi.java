package com.barbera.barberaconsumerapp.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;

public interface JsonPlaceHolderApi {
    @GET("")
    Call<Emailer> sendEmail(@Body Emailer emailer);
}

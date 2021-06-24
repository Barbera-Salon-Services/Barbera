package com.barbera.barberaconsumerapp.network_aws;

import com.barbera.barberaconsumerapp.R;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface JsonPlaceHolderApi2 {
    @POST("loginphone")
    Call<Register> getToken(@Body Register register);

    @POST("loginotp")
    Call<Register> checkOtp(@Body Register register, @Header("token") String token);

//    @POST("signup")
//    Call<Register> registerUser(@Body Register register,@Header("token") String token);
//
//    @POST("loginemail")
//    Call<Register> emailLogin(@Body Register register,@Header("token") String token);

//    @POST("loginpass")
//    Call<Register> passwordLogin(@Body Register register, @Header("token") String token);

}

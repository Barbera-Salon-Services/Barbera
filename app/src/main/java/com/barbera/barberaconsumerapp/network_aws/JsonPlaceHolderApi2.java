package com.barbera.barberaconsumerapp.network_aws;

import com.barbera.barberaconsumerapp.CartItemModel;
import com.barbera.barberaconsumerapp.R;
import com.barbera.barberaconsumerapp.Service;
import com.barbera.barberaconsumerapp.Utils.CartList;
import com.barbera.barberaconsumerapp.Utils.ServiceItem;
import com.barbera.barberaconsumerapp.Utils.ServiceList;
import com.barbera.barberaconsumerapp.Utils.TypeList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi2 {
    @POST("loginphone")
    Call<Register> getToken(@Body Register register);

    @POST("loginotp")
    Call<Register> checkOtp(@Body Register register, @Header("Authorization") String token);

    @POST("profileupd")
    Call<Register> updateProfile(@Body Register register, @Header("Authorization") String token);

    @GET("getuser")
    Call<Register> getProfile(@Header("Authorization") String token);

    @GET("gettrend")
    Call<ServiceList> getTrending(@Header("Authorization") String token);

    @GET("gettypes/{gender}")
    Call<TypeList> getTypes(@Path ("gender") String gender, @Header("Authorization") String token);

    @POST("getsubtypes/{gender}")
    Call<TypeList> getSubTypes(@Path ("gender") String gender,@Body ServiceItem service,@Header("Authorization") String token);

    @POST("getallserv/{gender}")
    Call<ServiceList> getAllServices(@Path ("gender") String gender,@Body ServiceItem service, @Header("Authorization") String token);

    @POST("addtocart/{serviceid}")
    Call<Success> addToCart(@Body Success model,@Header("Authorization") String token);

    @GET("getcart")
    Call<CartList> getCart(@Header("Authorization") String token);

    @POST("quantity/{seviceid}")
    Call<Void> updateQuantity(@Path("serviceid") String id,@Header("Authorization") String token);

    @POST("deletefromcart/{serviceid}")
    Call<Void> deleteFromCart(@Path("serviceid") String id,@Header("Authorization") String token);
}

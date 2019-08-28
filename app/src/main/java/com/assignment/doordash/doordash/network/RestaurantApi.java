package com.assignment.doordash.doordash.network;

import com.assignment.doordash.doordash.model.Restaurant;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface RestaurantApi {

    @GET("/v2/restaurant/")
    Call<Restaurant[]> getRestaurants(@QueryMap HashMap<String, String> queryMap);

    @GET
    Call<ResponseBody> getImage(@Url String url);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.doordash.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}

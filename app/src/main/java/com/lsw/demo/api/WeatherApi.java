package com.lsw.demo.api;


import com.lsw.demo.model.WeatherEntity;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface WeatherApi {
    @GET("weather")
    Call<WeatherEntity> getWeather(@Query("city") String city, @Query("key") String key);
}

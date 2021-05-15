package com.rikyahmadfathoni.test.weatherapp.data.rest;

import com.rikyahmadfathoni.test.weatherapp.data.model.WeatherCurrentModel;
import com.rikyahmadfathoni.test.weatherapp.data.model.WeatherForecastModel;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/*
* Using Open Weather Map
* */
public interface ApiService {

    @GET("weather")
    Observable<WeatherCurrentModel> getObservableCurrentBy(
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("appid") String appid,
            @Query("units") String units
    );

    @GET("weather")
    Call<WeatherCurrentModel> getCurrentBy(
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("appid") String appid,
            @Query("units") String units
    );

    @GET("weather")
    Observable<WeatherCurrentModel> getObservableCurrentBy(
            @Query("q") String cityName,
            @Query("appid") String appid,
            @Query("units") String units
    );

    @GET("weather")
    Call<WeatherCurrentModel> getCurrentBy(
            @Query("q") String cityName,
            @Query("appid") String appid,
            @Query("units") String units
    );

    @GET("onecall")
    Observable<WeatherForecastModel> getObservableForecastBy(
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("exclude") String exclude,
            @Query("appid") String appid,
            @Query("units") String units
    );

    @GET("onecall")
    Call<WeatherForecastModel> getForecastBy(
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("exclude") String exclude,
            @Query("appid") String appid,
            @Query("units") String units
    );

}

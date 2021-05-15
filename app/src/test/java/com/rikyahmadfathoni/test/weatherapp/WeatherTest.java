package com.rikyahmadfathoni.test.weatherapp;

import com.rikyahmadfathoni.test.weatherapp.data.model.WeatherCurrentModel;
import com.rikyahmadfathoni.test.weatherapp.data.model.WeatherForecastModel;
import com.rikyahmadfathoni.test.weatherapp.data.rest.ApiService;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.assertTrue;

public class WeatherTest {

    private static final String CITY = "solo";
    private static final String LATITUDE = "-7.4075707";
    private static final String LONGITUDE = "110.9441179";

    private ApiService apiService;

    @Before
    public void setUp() {
        this.apiService = new Retrofit.Builder()
                .baseUrl(Common.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(ApiService.class);
    }

    @Test
    public void getCurrentWeatherByCoordinates() {
        final Call<WeatherCurrentModel> call = apiService.getCurrentBy(
                LATITUDE, LONGITUDE, Common.APP_ID, Common.UNIT_METRIC
        );

        try {
            final Response<WeatherCurrentModel> response = call.execute();
            final WeatherCurrentModel weatherCurrentModel = response.body();

            if (weatherCurrentModel != null) {
                System.out.println("Current result by coordinates : " + weatherCurrentModel.toString());
            }

            assertTrue(response.isSuccessful() && weatherCurrentModel != null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getCurrentWeatherByCity() {
        final Call<WeatherCurrentModel> call = apiService.getCurrentBy(
                CITY, Common.APP_ID, Common.UNIT_METRIC
        );

        try {
            final Response<WeatherCurrentModel> response = call.execute();
            final WeatherCurrentModel weatherCurrentModel = response.body();

            if (weatherCurrentModel != null) {
                System.out.println("Current result by city : " + weatherCurrentModel.toString());
            }

            assertTrue(response.isSuccessful() && weatherCurrentModel != null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getForecastWeatherByCoordinates() {
        final Call<WeatherForecastModel> call = apiService.getForecastBy(
                LATITUDE, LONGITUDE, Common.INCLUDE_DAILY, Common.APP_ID, Common.UNIT_METRIC
        );

        try {
            final Response<WeatherForecastModel> response = call.execute();
            final WeatherForecastModel weatherForecastModel = response.body();

            if (weatherForecastModel != null) {
                System.out.println("Forecast result by coordinates : "
                        + weatherForecastModel.toString() + " | " + weatherForecastModel.getDaily().size());
            }

            assertTrue(response.isSuccessful() && weatherForecastModel != null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.rikyahmadfathoni.test.weatherapp.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rikyahmadfathoni.test.weatherapp.data.model.forecast.Current;
import com.rikyahmadfathoni.test.weatherapp.data.model.forecast.Daily;
import com.rikyahmadfathoni.test.weatherapp.util.UtilsDate;
import com.rikyahmadfathoni.test.weatherapp.util.UtilsGson;

public class WeatherForecastModel {

    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lon")
    @Expose
    private Double lon;
    @SerializedName("timezone")
    @Expose
    private String timezone;
    @SerializedName("timezone_offset")
    @Expose
    private Integer timezoneOffset;
    @SerializedName("current")
    @Expose
    private Current current;
    @SerializedName("daily")
    @Expose
    private List<Daily> daily = null;
    @SerializedName("validDaily")
    @Expose
    private List<Daily> validDaily = null;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Integer getTimezoneOffset() {
        return timezoneOffset;
    }

    public void setTimezoneOffset(Integer timezoneOffset) {
        this.timezoneOffset = timezoneOffset;
    }

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    public List<Daily> getDaily() {
        return daily;
    }

    public void setDaily(List<Daily> daily) {
        this.daily = daily;
    }

    @NonNull
    public List<Daily> getValidDaily() {
        if (validDaily == null) {
            final List<Daily> results = new ArrayList<>();
            if (daily != null) {
                final long currentTime = getCurrent().getDt() * 1000L;
                final String currentId = UtilsDate.getDateId(currentTime);
                for (Daily daily : daily) {
                    final long forecastTime = daily.getDt() * 1000L;
                    final String forecastId = UtilsDate.getDateId(forecastTime);
                    if (!currentId.equals(forecastId)) {
                        results.add(daily);
                    }
                }
            }
            validDaily = results;
        }
        return validDaily;
    }

    @Nullable
    public Daily getDaysDaily() {
        if (daily != null) {
            final String currentId = UtilsDate.getDateId(System.currentTimeMillis());
            for (Daily daily : daily) {
                final long forecastTime = daily.getDt() * 1000L;
                final String forecastId = UtilsDate.getDateId(forecastTime);
                if (currentId.equals(forecastId)) {
                    return daily;
                }
            }
        }
        return null;
    }

    @Override
    @NonNull
    public String toString() {
        final String json = UtilsGson.toJson(this);
        if (json != null) {
            return json;
        } else {
            return "WeatherForecast{" +
                    "lat=" + lat +
                    ", lon=" + lon +
                    ", timezone='" + timezone + '\'' +
                    ", timezoneOffset=" + timezoneOffset +
                    ", current=" + current +
                    ", daily=" + daily +
                    '}';
        }
    }
}

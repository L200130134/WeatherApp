package com.rikyahmadfathoni.test.weatherapp;

import android.location.Location;

public class Common {

    public static final String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    public static final String APP_ID = "ae0fadb081e48dfa1bb21ab6a04bcbe6";
    public static final String INCLUDE_HOURLY = "minutely,daily,alerts";
    public static final String INCLUDE_MINUTELY = "hourly,daily,alerts";
    public static final String INCLUDE_DAILY = "minutely,hourly,alerts";
    public static final String UNIT_STANDARD = "standard";
    public static final String UNIT_METRIC = "metric";
    public static final String UNIT_IMPERIAL = "imperial";

    public static Location currentLocation;

    public static final String[] WEATHER_STATUS = {
            "Thunderstorm",
            "Drizzle",
            "Rain",
            "Snow",
            "Atmosphere",
            "Clear",
            "Few Clouds",
            "Broken Clouds",
            "Cloud"
    };
}

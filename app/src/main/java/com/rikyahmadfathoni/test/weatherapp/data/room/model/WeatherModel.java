package com.rikyahmadfathoni.test.weatherapp.data.room.model;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.rikyahmadfathoni.test.weatherapp.data.model.WeatherCurrentModel;
import com.rikyahmadfathoni.test.weatherapp.data.model.WeatherForecastModel;
import com.rikyahmadfathoni.test.weatherapp.data.model.current.Weather;
import com.rikyahmadfathoni.test.weatherapp.data.model.forecast.Daily;
import com.rikyahmadfathoni.test.weatherapp.util.UtilsDialog;
import com.rikyahmadfathoni.test.weatherapp.util.UtilsGson;
import com.rikyahmadfathoni.test.weatherapp.util.UtilsInteger;
import com.rikyahmadfathoni.test.weatherapp.util.UtilsList;

@Entity(tableName = "weather")
public class WeatherModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("locationId")
    private String locationId;

    @SerializedName("name")
    private String name;

    @SerializedName("country")
    private String country;

    @SerializedName("weather")
    private String weather;

    @SerializedName("weatherId")
    private int weatherId;

    @SerializedName("desc")
    private String desc;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("temp")
    private double temp;

    @SerializedName("tempMin")
    private double tempMin;

    @SerializedName("tempMax")
    private double tempMax;

    @SerializedName("pressure")
    private double pressure;

    @SerializedName("humidity")
    private double humidity;

    @SerializedName("windchill")
    private double windchill;

    @SerializedName("windSpeed")
    private double windSpeed;

    @SerializedName("date")
    private long date;

    @SerializedName("forecastJson")
    private String forecastJson;

    public WeatherModel() {
        super();
    }

    @Ignore
    public WeatherModel(WeatherCurrentModel model, WeatherForecastModel forecastModel) {
        final Weather weather = UtilsList.getFirst(model.getWeather());
        this.name = model.getName();
        this.country = model.getSys().getCountry();
        this.locationId = String.valueOf(model.getId());
        this.latitude = String.valueOf(model.getCoord().getLat());
        this.longitude = String.valueOf(model.getCoord().getLon());
        this.temp = model.getMain().getTemp();
        this.tempMin = model.getMain().getTempMin();
        this.tempMax = model.getMain().getTempMax();
        this.pressure = model.getMain().getPressure();
        this.humidity = model.getMain().getHumidity();
        this.windchill = model.getWind().getDeg();
        this.windSpeed = model.getWind().getSpeed();
        this.date = model.getDt();
        this.forecastJson = UtilsGson.toJson(forecastModel);
        if (weather != null) {
            this.weather = weather.getMain();
            this.weatherId = weather.getId();
            this.desc = weather.getDescription();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public int getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getTempMin() {
        return tempMin;
    }

    public void setTempMin(double tempMin) {
        this.tempMin = tempMin;
    }

    public double getTempMax() {
        return tempMax;
    }

    public void setTempMax(double tempMax) {
        this.tempMax = tempMax;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getWindchill() {
        return windchill;
    }

    public void setWindchill(double windchill) {
        this.windchill = windchill;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getForecastJson() {
        return forecastJson;
    }

    public void setForecastJson(String forecastJson) {
        this.forecastJson = forecastJson;
    }

    @Nullable
    public WeatherCurrentModel getCurrent() {
        try {
            final WeatherForecastModel wfm = getForecast();
            if (wfm != null) {
                final Daily daily = wfm.getDaysDaily();
                if (daily != null) {
                    final WeatherCurrentModel currentModel = WeatherCurrentModel.getNonNullInstance();
                    currentModel.setId(UtilsInteger.parseInt(locationId));
                    currentModel.setName(name);
                    currentModel.getSys().setCountry(country);
                    currentModel.getWeather().get(0).setMain(weather);
                    currentModel.getWeather().get(0).setId(weatherId);
                    currentModel.getWeather().get(0).setDescription(desc);
                    currentModel.getCoord().setLat(UtilsInteger.parseDouble(latitude));
                    currentModel.getCoord().setLon(UtilsInteger.parseDouble(longitude));
                    currentModel.getMain().setTemp(temp);
                    currentModel.getMain().setTempMin(tempMin);
                    currentModel.getMain().setTempMax(tempMax);
                    currentModel.getMain().setPressure((int) pressure);
                    currentModel.getMain().setHumidity((int) humidity);
                    currentModel.getWind().setDeg(windchill);
                    currentModel.getWind().setSpeed(windSpeed);
                    currentModel.setDt((int) date);
                    return currentModel;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public WeatherForecastModel getForecast() {
        try {
            return UtilsGson.toObject(forecastJson, WeatherForecastModel.class);
        } catch (Exception e) {
            e.printStackTrace();
            UtilsDialog.postToast("Gagal convert json : " + e.getLocalizedMessage());
        }
        return null;
    }
}

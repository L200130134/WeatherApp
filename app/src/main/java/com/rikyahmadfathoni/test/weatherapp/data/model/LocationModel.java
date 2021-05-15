package com.rikyahmadfathoni.test.weatherapp.data.model;

import android.location.Location;

import com.google.android.gms.location.LocationResult;
import com.rikyahmadfathoni.test.weatherapp.data.model.current.Coord;
import com.rikyahmadfathoni.test.weatherapp.util.UtilsList;
import com.rikyahmadfathoni.test.weatherapp.util.UtilsString;

public class LocationModel {

    private String latitude;
    private String longitude;

    public LocationModel(Location location) {
        if (location != null) {
            this.latitude = String.valueOf(location.getLatitude());
            this.longitude = String.valueOf(location.getLongitude());
        }
    }

    public LocationModel(Coord coord) {
        if (coord != null) {
            this.latitude = String.valueOf(coord.getLat());
            this.longitude = String.valueOf(coord.getLon());
        }
    }

    public LocationModel(Double latitude, Double longitude) {
        if (latitude != null) {
            this.latitude = String.valueOf(latitude);
        }
        if (longitude != null) {
            this.longitude = String.valueOf(longitude);
        }
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public boolean isValid() {
        return !UtilsString.isEmpty(latitude) && !UtilsString.isEmpty(longitude);
    }

    @Override
    public String toString() {
        return "{" + "latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' + '}';
    }
}

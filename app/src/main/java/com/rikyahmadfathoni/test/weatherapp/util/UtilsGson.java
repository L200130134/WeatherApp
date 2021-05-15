package com.rikyahmadfathoni.test.weatherapp.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rikyahmadfathoni.test.weatherapp.data.model.WeatherCurrentModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public class UtilsGson {

    @NonNull
    public static <T> ArrayList<T> toList(String json, Type type) {
        try {
            if (json != null) {
                return new Gson().fromJson(json, type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @NonNull
    public static <T> ArrayList<T> toList(String json, Class<T[]> clazz) {
        try {
            if (json != null) {
                T[] array = new Gson().fromJson(json, clazz);
                return new ArrayList<>(Arrays.asList(array));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Nullable
    public static WeatherCurrentModel toObject(String json) {
        try {
            return new Gson().fromJson(json, WeatherCurrentModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static <T> T toObject(String json, Class<T> mClass) {
        try {
            return new Gson().fromJson(json, mClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static <T> T toObject(String json, Type type) {
        try {
            return new Gson().fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static <T> String toJson(ArrayList<T> objectList) {
        try {
            if (objectList != null) {
                Type type = new TypeToken<ArrayList<T>>() {
                }.getType();
                return new Gson().toJson(objectList, type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static <T> String toJson(T object) {
        try {
            if (object != null) {
                return new Gson().toJson(object, object.getClass());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> Type getType() {
        return new TypeToken<ArrayList<T>>() {
        }.getType();
    }
}

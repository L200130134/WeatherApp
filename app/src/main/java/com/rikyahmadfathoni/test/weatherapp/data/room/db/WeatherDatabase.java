package com.rikyahmadfathoni.test.weatherapp.data.room.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.rikyahmadfathoni.test.weatherapp.data.model.current.Weather;
import com.rikyahmadfathoni.test.weatherapp.data.room.dao.WeatherDao;
import com.rikyahmadfathoni.test.weatherapp.data.room.model.WeatherModel;

@Database(entities = {WeatherModel.class}, version = 1)
public abstract class WeatherDatabase extends RoomDatabase {

    private static WeatherDatabase instance;

    public abstract WeatherDao weatherDao();

    public static synchronized WeatherDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    WeatherDatabase.class, "WeatherDatabase")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private final static Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };
}
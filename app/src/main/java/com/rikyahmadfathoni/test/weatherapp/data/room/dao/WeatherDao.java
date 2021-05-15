package com.rikyahmadfathoni.test.weatherapp.data.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.rikyahmadfathoni.test.weatherapp.data.room.model.WeatherModel;

import java.util.List;

@Dao
public interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WeatherModel weatherModel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<WeatherModel> weatherModels);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(WeatherModel weatherModel);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(List<WeatherModel> weatherModels);

    @Delete
    void delete(WeatherModel weatherModel);

    @Delete
    void delete(List<WeatherModel> weatherModels);

    @Query("DELETE FROM weather WHERE locationId=:locationId")
    void delete(String locationId);

    @Query("DELETE FROM weather")
    void deleteAll();

    @Query("SELECT COUNT(id) FROM weather")
    int getItemCount();

    @Query("SELECT * FROM weather WHERE locationId=:locationId")
    List<WeatherModel> getItemsByLocationId(String locationId);

    @Query("SELECT * FROM weather WHERE name=:name")
    List<WeatherModel> getItemsByName(String name);

    @Query("SELECT * FROM weather")
    List<WeatherModel> getItems();

    @Query("SELECT * FROM weather")
    LiveData<List<WeatherModel>> getLiveItems();
}
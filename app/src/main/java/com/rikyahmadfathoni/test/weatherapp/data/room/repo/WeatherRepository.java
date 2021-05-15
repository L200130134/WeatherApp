package com.rikyahmadfathoni.test.weatherapp.data.room.repo;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.rikyahmadfathoni.test.weatherapp.data.room.dao.WeatherDao;
import com.rikyahmadfathoni.test.weatherapp.data.room.db.WeatherDatabase;
import com.rikyahmadfathoni.test.weatherapp.data.room.model.WeatherModel;
import com.rikyahmadfathoni.test.weatherapp.util.UtilsDialog;
import com.rikyahmadfathoni.test.weatherapp.util.UtilsThread;

import java.util.List;

public class WeatherRepository implements WeatherDao {

    private final WeatherDao weatherDao;

    public WeatherRepository(Application application) {
        WeatherDatabase database = WeatherDatabase.getInstance(application);
        weatherDao = database.weatherDao();
    }

    @Override
    public void insert(WeatherModel weatherModel) {
        UtilsThread.runOnThread(() -> {
            weatherDao.deleteAll();
            weatherDao.insert(weatherModel);
        });
    }

    @Override
    public void insert(List<WeatherModel> weatherModels) {
        UtilsThread.runOnThread(() -> {
            weatherDao.insert(weatherModels);
        });
    }

    @Override
    public void update(WeatherModel weatherModel) {
        UtilsThread.runOnThread(() -> {
            weatherDao.update(weatherModel);
        });
    }

    @Override
    public void update(List<WeatherModel> weatherModels) {
        UtilsThread.runOnThread(() -> {
            weatherDao.update(weatherModels);
        });
    }

    @Override
    public void delete(WeatherModel weatherModel) {
        UtilsThread.runOnThread(() -> {
            weatherDao.delete(weatherModel);
        });
    }

    @Override
    public void delete(List<WeatherModel> weatherModels) {
        UtilsThread.runOnThread(() -> {
            weatherDao.delete(weatherModels);
        });
    }

    @Override
    public void delete(String locationId) {
        UtilsThread.runOnThread(() -> {
            weatherDao.delete(locationId);
        });
    }

    @Override
    public void deleteAll() {
        UtilsThread.runOnThread(weatherDao::deleteAll);
    }

    @Override
    public int getItemCount() {
        return weatherDao.getItemCount();
    }

    @Override
    public List<WeatherModel> getItemsByLocationId(String locationId) {
        return weatherDao.getItemsByLocationId(locationId);
    }

    @Override
    public List<WeatherModel> getItemsByName(String name) {
        return weatherDao.getItemsByName(name);
    }

    @Override
    public List<WeatherModel> getItems() {
        return weatherDao.getItems();
    }

    @Override
    public LiveData<List<WeatherModel>> getLiveItems() {
        return weatherDao.getLiveItems();
    }
}
package com.rikyahmadfathoni.test.weatherapp.data.room.repo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.rikyahmadfathoni.test.weatherapp.data.room.dao.WeatherDao;
import com.rikyahmadfathoni.test.weatherapp.data.room.model.WeatherModel;

import java.util.ArrayList;
import java.util.List;

public class WeatherViewModel extends AndroidViewModel implements WeatherDao {

    public static WeatherViewModel getInstance(Application application) {
        return new ViewModelProvider.AndroidViewModelFactory(application)
                .create(WeatherViewModel.class);
    }

    @Deprecated
    public static WeatherViewModel getInstance(@NonNull ViewModelStoreOwner owner) {
        return new ViewModelProvider(owner, new ViewModelProvider.NewInstanceFactory())
                .get(WeatherViewModel.class);
    }

    private final WeatherRepository repository;

    public WeatherViewModel(Application application) {
        super(application);
        this.repository = new WeatherRepository(application);
    }

    @Override
    public void insert(WeatherModel weatherModel) {
        repository.insert(weatherModel);
    }

    @Override
    public void insert(List<WeatherModel> weatherModels) {
        repository.insert(weatherModels);
    }

    @Override
    public void update(WeatherModel weatherModel) {
        repository.update(weatherModel);
    }

    @Override
    public void update(List<WeatherModel> weatherModels) {
        repository.update(weatherModels);
    }

    @Override
    public void delete(WeatherModel weatherModel) {
        repository.delete(weatherModel);
    }

    @Override
    public void delete(List<WeatherModel> weatherModels) {
        repository.delete(weatherModels);
    }

    @Override
    public void delete(String locationId) {
        repository.delete(locationId);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public int getItemCount() {
        return repository.getItemCount();
    }

    @Override
    public List<WeatherModel> getItemsByLocationId(String locationId) {
        return repository.getItemsByLocationId(locationId);
    }

    @Override
    public List<WeatherModel> getItemsByName(String name) {
        return repository.getItemsByName(name);
    }

    @Override
    public List<WeatherModel> getItems() {
        return repository.getItems();
    }

    @Override
    public LiveData<List<WeatherModel>> getLiveItems() {
        return repository.getLiveItems();
    }
}
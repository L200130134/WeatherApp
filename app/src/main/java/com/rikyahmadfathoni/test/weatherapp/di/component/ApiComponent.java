package com.rikyahmadfathoni.test.weatherapp.di.component;

import javax.inject.Singleton;
import dagger.Component;

import com.rikyahmadfathoni.test.weatherapp.di.module.ApiModule;
import com.rikyahmadfathoni.test.weatherapp.di.module.AppModule;
import com.rikyahmadfathoni.test.weatherapp.ui.activity.*;

@Singleton
@Component(modules = {AppModule.class, ApiModule.class})
public interface ApiComponent {
    void inject(MainActivity activity);
}
package com.rikyahmadfathoni.test.weatherapp.di.module;

import android.app.Application;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final Application mApplication;

    public AppModule(Application mApplication) {
        this.mApplication = mApplication;
    }

    @Provides
    @Singleton
    public Application provideApplication() {
        return mApplication;
    }
}

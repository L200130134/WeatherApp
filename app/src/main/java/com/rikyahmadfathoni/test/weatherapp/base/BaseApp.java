package com.rikyahmadfathoni.test.weatherapp.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.rikyahmadfathoni.test.weatherapp.Common;
import com.rikyahmadfathoni.test.weatherapp.di.component.ApiComponent;
import com.rikyahmadfathoni.test.weatherapp.di.component.DaggerApiComponent;
import com.rikyahmadfathoni.test.weatherapp.di.module.ApiModule;
import com.rikyahmadfathoni.test.weatherapp.di.module.AppModule;

public class BaseApp extends Application {

    private static BaseApp instance;

    public static BaseApp getInstance() {
        return instance;
    }

    public static Resources getRes() {
        return instance.getResources();
    }

    public static RequestManager getRequestManager() {
        return instance.mRequestManager;
    }

    public static ApiComponent getApiComponent() {
        return instance.mApiComponent;
    }

    private RequestManager mRequestManager;

    private ApiComponent mApiComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        mApiComponent = DaggerApiComponent.builder()
                .appModule(new AppModule(this))
                .apiModule(new ApiModule(Common.BASE_URL))
                .build();

        mRequestManager = Glide.with(this)
                .applyDefaultRequestOptions(new RequestOptions()
                        //.placeholder(R.drawable.placeholder)
                        .circleCrop());
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }

}

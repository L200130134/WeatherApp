package com.rikyahmadfathoni.test.weatherapp.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.transition.TransitionManager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.rikyahmadfathoni.test.weatherapp.Common;
import com.rikyahmadfathoni.test.weatherapp.R;
import com.rikyahmadfathoni.test.weatherapp.adapter.ForecastAdapter;
import com.rikyahmadfathoni.test.weatherapp.base.BaseActivity;
import com.rikyahmadfathoni.test.weatherapp.base.BaseApp;
import com.rikyahmadfathoni.test.weatherapp.data.model.LocationModel;
import com.rikyahmadfathoni.test.weatherapp.data.model.RectModel;
import com.rikyahmadfathoni.test.weatherapp.data.model.WeatherCurrentModel;
import com.rikyahmadfathoni.test.weatherapp.data.model.WeatherForecastModel;
import com.rikyahmadfathoni.test.weatherapp.data.model.current.Weather;
import com.rikyahmadfathoni.test.weatherapp.data.model.forecast.Daily;
import com.rikyahmadfathoni.test.weatherapp.data.rest.ApiService;
import com.rikyahmadfathoni.test.weatherapp.data.room.model.WeatherModel;
import com.rikyahmadfathoni.test.weatherapp.data.room.repo.WeatherViewModel;
import com.rikyahmadfathoni.test.weatherapp.databinding.ActivityMainBinding;
import com.rikyahmadfathoni.test.weatherapp.ui.view.WeatherDetailView;
import com.rikyahmadfathoni.test.weatherapp.util.UtilsApp;
import com.rikyahmadfathoni.test.weatherapp.util.UtilsDate;
import com.rikyahmadfathoni.test.weatherapp.util.UtilsDialog;
import com.rikyahmadfathoni.test.weatherapp.util.UtilsList;
import com.rikyahmadfathoni.test.weatherapp.util.UtilsString;
import com.rikyahmadfathoni.test.weatherapp.util.UtilsSystem;
import com.rikyahmadfathoni.test.weatherapp.util.UtilsThread;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements MultiplePermissionsListener,
        View.OnClickListener, SwipeRefreshLayout.OnRefreshListener,
        ForecastAdapter.EventListener {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private ActivityMainBinding binding;
    private ForecastAdapter forecastAdapter;
    private boolean currentLoaded;
    private boolean forecastLoaded;
    private WeatherCurrentModel weatherCurrentModel;
    private WeatherForecastModel weatherForecastModel;
    private WeatherViewModel weatherViewModel;

    @Inject
    ApiService apiService;

    @Override
    protected View getContentView() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BaseApp.getApiComponent().inject(this);
        bindObject();
        bindView();
        bindEvent();
        bindAdapter();
        bindCardWeather();
        getDataFromDb();
    }

    private void bindObject() {
        this.weatherViewModel = WeatherViewModel.getInstance(getApplication());
    }

    private void bindView() {
        this.setRefreshing(true, true);
    }

    private void bindEvent() {
        binding.header.iconMenu.setOnClickListener(this);
        binding.header.iconSearch.setOnClickListener(this);
        binding.header.iconClose.setOnClickListener(this);
        binding.cardShimmer.setOnClickListener(this);
        binding.cardCurrentWeather.setOnClickListener(this);
        binding.swipeRefresh.setOnRefreshListener(this);
    }

    private void bindAdapter() {
        this.forecastAdapter = new ForecastAdapter();
        this.forecastAdapter.setEventListener(this);
        this.forecastAdapter.setEnablePlaceholder(true);
        this.binding.listForecast.setLayoutManager(
                new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        );
        this.binding.listForecast.setAdapter(forecastAdapter);
    }

    private void bindCardWeather() {
        UtilsApp.setBackgroundCard(
                binding.cardCurrentWeather.getBackground(),
                System.currentTimeMillis()
        );
    }

    private void getDataFromDb() {
        UtilsThread.runOnThread(() -> {
           final List<WeatherModel> wl = weatherViewModel.getItems();
           final WeatherModel weatherModel = UtilsList.getFirst(wl);
           if (weatherModel != null) {
               this.weatherCurrentModel = weatherModel.getCurrent();
               this.weatherForecastModel = weatherModel.getForecast();
               runOnUiThread(() -> {
                   try {
                       if (weatherCurrentModel != null) {
                           bindCurrentWeather(weatherCurrentModel, false);
                           if (weatherForecastModel != null) {
                               bindForecastWeather(weatherForecastModel, false);
                           }
                       }
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               });
           }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.resetLoadedProcess();
        this.requestPermission();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @Override
    public void onPermissionsChecked(MultiplePermissionsReport report) {
        if (report.areAllPermissionsGranted()) {
            buildLocationRequest();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        }
    }

    @Override
    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list,
                                                   PermissionToken permissionToken) {

    }

    @Override
    public void onRefresh() {
        setRefreshing(false, false);
        this.getCurrentWeather(new LocationModel(Common.currentLocation), true);
    }

    @Override
    public void onItemClick(View view, @Nullable Daily daily, int position) {
        final WeatherDetailView wdv = WeatherDetailView.getView(view, binding.getRoot());
        if (wdv != null) {
            wdv.setDaily(daily);
            wdv.startAnimationEnter();
        }
    }

    @Override
    public void onClick(View view) {
        if (binding == null) {
            return;
        }
        if (view == binding.header.iconMenu) {
            UtilsDialog.postToast("Unavailable menu");
        } else if (view == binding.header.iconSearch) {
            searchModeView(true);
        } else if (view == binding.header.iconClose) {
            searchModeView(false);
            compositeDisposable.clear();
            setRefreshing(false, true);
        } else if (view == binding.cardCurrentWeather) {
            if (weatherCurrentModel != null) {
                final WeatherDetailView wdv = WeatherDetailView.getView(view, binding.getRoot());
                if (wdv != null) {
                    wdv.setCurrentModel(weatherCurrentModel);
                    wdv.startAnimationEnter();
                }
            }
        }
    }

    private void requestPermission() {
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                )
                .withListener(this)
                .check();
    }

    private void buildLocationRequest() {
        if (locationRequest == null) {
            this.locationRequest = LocationRequest.create();
            this.locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            this.locationRequest.setInterval(5000);
            this.locationRequest.setFastestInterval(3000);
            this.locationRequest.setSmallestDisplacement(10f);

            this.fusedLocationProviderClient = LocationServices
                    .getFusedLocationProviderClient(this);
        }
    }

    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);

            Common.currentLocation = locationResult.getLastLocation();

            getCurrentWeather(new LocationModel(Common.currentLocation), false);

            System.err.println("Location : " +
                    "Lat : " + locationResult.getLastLocation().getLatitude()
                    + " | Lon : "  + locationResult.getLastLocation().getLongitude()
            );
        }

        @Override
        public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) {
            super.onLocationAvailability(locationAvailability);
        }
    };

    private void bindCurrentWeather(WeatherCurrentModel model, boolean insertDb) {
        if (model != null) {
            if (insertDb) {
                this.weatherCurrentModel = model;
            }
            final Weather weather = UtilsList.getFirst(model.getWeather());
            binding.header.textCity.setText(model.getName());
            binding.textDate.setText(UtilsDate.getSimpleDate(model.getDt() * 1000L));
            binding.textTemp.setText(String.format("%s°", Math.round(model.getMain().getTemp())));
            binding.textHumidity.setText(String.format("%s%%", model.getMain().getHumidity()));
            binding.titleHumidity.setText(R.string.text_humidity);
            if (weather != null) {
                //binding.textInfo.setText(UtilsApp.getWeatherStatus(weather.getId()));
                binding.textInfo.setText(weather.getMain());
                binding.animationWeather.setAnimation(UtilsApp.getWeatherAnimation(weather.getId()));
                binding.animationWeather.playAnimation();
            }
            TransitionManager.beginDelayedTransition(binding.content);
            setRefreshing(false, insertDb);
            binding.textDate.setVisibility(View.VISIBLE);
            currentLoaded = true;
            if (isSearchMode()) {
                getForecastWeather(new LocationModel(model.getCoord()), true);
            }
            if (insertDb) {
                tryToInsertDb();
            }
        } else {
            UtilsDialog.postToast("Invalid current data result");
        }
    }

    private boolean isSearchMode() {
        return binding.header.inputSearch.getVisibility() == View.VISIBLE;
    }

    private void bindForecastWeather(WeatherForecastModel model, boolean insertDb) {
        if (model != null) {
            this.weatherForecastModel = model;
            binding.textTitleForecast.setText(String.format("Next %s days", model.getValidDaily().size()));
            if (forecastAdapter != null) {
                this.forecastAdapter.setEnablePlaceholder(!insertDb);
                this.forecastAdapter.setItems(model);
                this.forecastAdapter.notifyDataSetChanged();
            }
            forecastLoaded = true;
            if (isSearchMode()) {
                searchModeView(false);
            }
            if (insertDb) {
                tryToInsertDb();
            }
        } else {
            UtilsDialog.postToast("Invalid forecast data result");
        }
    }

    private void tryToInsertDb() {
        if (weatherCurrentModel != null && weatherForecastModel != null) {
            weatherViewModel.insert(new WeatherModel(weatherCurrentModel, weatherForecastModel));
        }
    }

    private void searchModeView(boolean enable) {
        if (enable && isSearchMode()) {
            doSearch();
            return;
        }

        TransitionManager.beginDelayedTransition(binding.header.getRoot());

        binding.header.textCity.setVisibility(enable ? View.GONE : View.VISIBLE);
        binding.header.inputSearch.setVisibility(enable ? View.VISIBLE : View.GONE);
        binding.header.iconMenu.setVisibility(enable ? View.GONE : View.VISIBLE);
        binding.header.iconClose.setVisibility(enable ? View.VISIBLE : View.GONE);

        if (enable) {
            binding.header.inputSearch.requestFocus();
            binding.header.inputSearch.performClick();
            UtilsSystem.showSoftKeyboard(this, binding.header.inputSearch);
        } else {
            UtilsSystem.hideKeyboard(this, binding.header.inputSearch);
        }
    }

    private void doSearch() {
        final String inputValue = binding.header.inputSearch.getText().toString();
        if (!UtilsString.isEmpty(inputValue)) {
            searchCurrentWeather(inputValue);
        }
    }

    private void resetLoadedProcess() {
        this.currentLoaded = false;
        this.forecastLoaded = false;
    }

    private void getCurrentWeather(final LocationModel locationModel, boolean force) {
        if (currentLoaded && !force) {
            return;
        }
        if (locationModel != null && locationModel.isValid()) {
            final Observable<WeatherCurrentModel> observable = apiService.getObservableCurrentBy(
                    String.valueOf(locationModel.getLatitude()),
                    String.valueOf(locationModel.getLongitude()),
                    Common.APP_ID, Common.UNIT_METRIC
            );

            final Disposable dos = observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(model -> {
                        bindCurrentWeather(model, true);
                    }, throwable -> {
                        UtilsDialog.postToast("No current data found by coordinates \""
                                + locationModel.toString() + "\"");
                        setRefreshing(false, true);
                    });

            compositeDisposable.add(dos);
        }
        getForecastWeather(locationModel, force);
    }

    private void getForecastWeather(final LocationModel locationModel, boolean force) {
        if (forecastLoaded && !force) {
            return;
        }
        if (locationModel != null && locationModel.isValid()) {
            final Observable<WeatherForecastModel> observable = apiService.getObservableForecastBy(
                    String.valueOf(locationModel.getLatitude()),
                    String.valueOf(locationModel.getLongitude()),
                    Common.INCLUDE_DAILY,
                    Common.APP_ID, Common.UNIT_METRIC
            );

            final Disposable dos = observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(model -> {
                        bindForecastWeather(model, true);
                    }, throwable -> {
                        UtilsDialog.postToast("No forecast data found by coordinates \""
                                + locationModel.toString() + "\"");
                        setRefreshing(false, true);
                    });

            compositeDisposable.add(dos);
        }
    }

    private void searchCurrentWeather(String cityName) {
        if (!UtilsString.isEmpty(cityName)) {
            UtilsSystem.hideKeyboard(this, binding.header.inputSearch);
            UtilsDialog.postToast("Search by city \"" + cityName + "\"");
            final Observable<WeatherCurrentModel> observable = apiService.getObservableCurrentBy(
                    cityName.toLowerCase(),
                    Common.APP_ID, Common.UNIT_METRIC
            );

            final Disposable dos = observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(model -> {
                        bindCurrentWeather(model, false);
                    }, throwable -> {
                        binding.header.textCity.setText(String.format("Unknown \"%s\"", cityName));
                        UtilsDialog.postToast("No current data found by city \"" + cityName + "\"");
                        setRefreshing(false, true);
                    });

            compositeDisposable.add(dos);
            binding.header.textCity.setText("Searching...");
            searchModeView(false);
            setRefreshing(true, true);
        }
    }

    private void setRefreshing(boolean refreshing, boolean doShimmer) {
        if (binding != null) {
            binding.swipeRefresh.post(() -> {
                binding.swipeRefresh.setRefreshing(refreshing);
            });
            if (doShimmer) {
                if (refreshing) {
                    binding.cardShimmer.showShimmer(true);
                } else {
                    binding.cardShimmer.stopShimmer();
                    binding.cardShimmer.hideShimmer();
                }
            }
        }
    }
}
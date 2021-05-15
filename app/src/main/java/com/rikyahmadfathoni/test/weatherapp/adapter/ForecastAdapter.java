package com.rikyahmadfathoni.test.weatherapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.rikyahmadfathoni.test.weatherapp.Common;
import com.rikyahmadfathoni.test.weatherapp.R;
import com.rikyahmadfathoni.test.weatherapp.data.model.WeatherForecastModel;
import com.rikyahmadfathoni.test.weatherapp.data.model.current.Weather;
import com.rikyahmadfathoni.test.weatherapp.data.model.forecast.Daily;
import com.rikyahmadfathoni.test.weatherapp.util.UtilsApp;
import com.rikyahmadfathoni.test.weatherapp.util.UtilsDate;
import com.rikyahmadfathoni.test.weatherapp.util.UtilsList;

import java.util.ArrayList;
import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {

    private List<Daily> models = new ArrayList<>();
    private WeatherForecastModel forecastModel;
    private int dayOfWeek;
    private boolean enablePlaceholder;

    public ForecastAdapter() {
        this.dayOfWeek = UtilsDate.getDayOfWeek(System.currentTimeMillis());
    }

    public void setEnablePlaceholder(boolean enablePlaceholder) {
        this.enablePlaceholder = enablePlaceholder;
    }

    public void setItems(WeatherForecastModel model) {
        this.forecastModel = model;
        this.models = model.getValidDaily();
        this.dayOfWeek = UtilsDate.getDayOfWeek(System.currentTimeMillis());
    }

    public WeatherForecastModel getForecastModel() {
        return forecastModel;
    }

    public List<Daily> getItems() {
        return models;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_forecast, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(getItem(position));
    }

    private Daily getItem(int position) {
        return UtilsList.get(models, position);
    }

    @Override
    public int getItemCount() {
        return enablePlaceholder && models.isEmpty()
                ? UtilsDate.DAYS_OF_WEEK.length : models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView textDays;
        private final TextView textTemp;
        private final TextView textTempMin;
        private final TextView textTempMax;
        private final LinearLayout content;
        private final ShimmerFrameLayout shimmer;
        private final LottieAnimationView animationWeather;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textDays = itemView.findViewById(R.id.text_days);
            textTemp = itemView.findViewById(R.id.text_temp);
            textTempMin = itemView.findViewById(R.id.text_temp_min);
            textTempMax = itemView.findViewById(R.id.text_temp_max);
            animationWeather = itemView.findViewById(R.id.animation_weather);
            content = itemView.findViewById(R.id.content);
            shimmer = itemView.findViewById(R.id.card_shimmer);

            content.setOnClickListener(this);
        }

        public void bindData(@Nullable Daily item) {
            if (item != null) {
                final Weather weather = UtilsList.getFirst(item.getWeather());
                final long date = item.getDt() * 1000L;
                final String dayName = UtilsDate.getDayName(date);

                if (weather != null) {
                    animationWeather.setAnimation(UtilsApp.getWeatherAnimation(weather.getId()));
                    animationWeather.playAnimation();
                }

                if (dayName != null) {
                    textDays.setText(dayName);
                }

                textTemp.setText(UtilsApp.getTempFormat(item.getTemp().getDay()));
                textTempMin.setText(UtilsApp.getTempFormat(item.getTemp().getMin()));
                textTempMax.setText(UtilsApp.getTempFormat(item.getTemp().getMax()));

                UtilsApp.setBackgroundCard(
                        content.getBackground(),
                        date
                );

                if (!enablePlaceholder) {
                    shimmer.stopShimmer();
                    shimmer.hideShimmer();
                }
            } else {
                UtilsApp.setBackgroundCard(
                        content.getBackground(),
                        UtilsApp.getBackgroundColorByPosition(dayOfWeek + getAdapterPosition() + 1)
                );
                shimmer.showShimmer(true);
            }
        }

        @Override
        public void onClick(View view) {
            if (view == content) {
                final int position = getAdapterPosition();
                final Daily daily = getItem(position);
                if (daily != null) {
                    if (eventListener != null) {
                        eventListener.onItemClick(view, daily, position);
                    }
                }
            }
        }
    }

    public interface EventListener {
        void onItemClick(View view, @Nullable Daily daily, int position);
    }

    private EventListener eventListener;

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }
}

package com.rikyahmadfathoni.test.weatherapp.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.rikyahmadfathoni.test.weatherapp.R;
import com.rikyahmadfathoni.test.weatherapp.adapter.ForecastDetailsAdapter;
import com.rikyahmadfathoni.test.weatherapp.data.model.DetailsModel;
import com.rikyahmadfathoni.test.weatherapp.data.model.RectModel;
import com.rikyahmadfathoni.test.weatherapp.data.model.WeatherCurrentModel;
import com.rikyahmadfathoni.test.weatherapp.data.model.current.Weather;
import com.rikyahmadfathoni.test.weatherapp.data.model.forecast.Daily;
import com.rikyahmadfathoni.test.weatherapp.ui.animation.BounceInterpolator;
import com.rikyahmadfathoni.test.weatherapp.util.UtilsApp;
import com.rikyahmadfathoni.test.weatherapp.util.UtilsConvert;
import com.rikyahmadfathoni.test.weatherapp.util.UtilsDate;
import com.rikyahmadfathoni.test.weatherapp.util.UtilsList;

import java.util.ArrayList;
import java.util.List;

public class WeatherDetailView extends CoordinatorLayout implements View.OnClickListener {

    private static final long ANIMATION_ENTER_DURATION = 300;
    private static final long ANIMATION_EXIT_DURATION = 300;

    @Nullable
    public static WeatherDetailView getView(final View cuurentView, final ViewGroup parentView) {
        if (cuurentView != null && parentView != null) {
            final RectModel rectModel = new RectModel(cuurentView);
            final WeatherDetailView wdv = new WeatherDetailView(cuurentView.getContext(), rectModel);
            parentView.addView(wdv);
            return wdv;
        }
        return null;
    }

    public ImageView iconClose;
    public NestedScrollView scroller;
    public RelativeLayout container;
    public LinearLayout content;
    public TextView textDay;
    public TextView textTemp;
    public TextView textTempMin;
    public TextView textTempMax;
    public LinearLayout contentTemp;
    public LottieAnimationView animationWeather;
    public RecyclerView contentDetails;
    public ForecastDetailsAdapter detailsAdapter;
    private RectModel rectModel;
    private WeatherCurrentModel currentModel;
    private Daily daily;
    private boolean isAnimationEnterRun;
    private boolean isAnimationExitRun;

    public WeatherDetailView(@NonNull Context context) {
        super(context);
    }

    private WeatherDetailView(@NonNull Context context, RectModel rectModel) {
        super(context);
        this.rectModel = rectModel;
        this.createWeatherAnimation(context);
        this.createTextDays(context);
        this.createTextTemp(context);
        this.createTextTempMin(context);
        this.createTextTempMax(context);
        this.createContentTemp(context);
        this.createContentDetails(context);
        this.createContent(context);
        this.createScroller(context);
        this.createContainer(context);
        this.createIconClose(context);
        this.init();

        iconClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == iconClose) {
            startAnimationExit();
        }
    }

    private void createWeatherAnimation(Context context) {
        this.animationWeather = new LottieAnimationView(context);

        final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );

        lp.height = UtilsConvert.convertDpToPixel(150);
        lp.width = UtilsConvert.convertDpToPixel(150);
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        lp.topMargin = UtilsConvert.convertDpToPixel(20);
        this.animationWeather.setLayoutParams(lp);
    }

    private void createTextDays(Context context) {
        this.textDay = new TextView(context);
        this.textDay.setTextColor(ContextCompat.getColor(context, R.color.white));
        this.textDay.setTextSize(30f);
        this.textDay.setTypeface(ResourcesCompat.getFont(context, R.font.productsans_bold));

        final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        lp.topMargin = UtilsConvert.convertDpToPixel(70);
        this.textDay.setLayoutParams(lp);
    }

    private void createTextTemp(Context context) {
        this.textTemp = new TextView(context);
        this.textTemp.setTextColor(ContextCompat.getColor(context, R.color.white));
        this.textTemp.setTextSize(60f);
        this.textTemp.setTypeface(ResourcesCompat.getFont(context, R.font.productsans_bold));

        final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        lp.topMargin = UtilsConvert.convertDpToPixel(20);
        this.textTemp.setLayoutParams(lp);
    }

    private void createTextTempMin(Context context) {
        this.textTempMin = new TextView(context);
        this.textTempMin.setTextColor(ContextCompat.getColor(context, R.color.white));
        this.textTempMin.setTextSize(30f);
        this.textTempMin.setTypeface(ResourcesCompat.getFont(context, R.font.productsans_bold));
        this.textTempMin.setAlpha(0.7f);
        this.textTempMin.setGravity(Gravity.CENTER);

        final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        lp.weight = 1;
        this.textTempMin.setLayoutParams(lp);
    }

    private void createTextTempMax(Context context) {
        this.textTempMax = new TextView(context);
        this.textTempMax.setTextColor(ContextCompat.getColor(context, R.color.white));
        this.textTempMax.setTextSize(30f);
        this.textTempMax.setTypeface(ResourcesCompat.getFont(context, R.font.productsans_bold));
        this.textTempMax.setGravity(Gravity.CENTER);

        final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        lp.gravity = Gravity.CENTER_HORIZONTAL;
        lp.weight = 1;
        this.textTempMax.setLayoutParams(lp);
    }

    private void createContentTemp(Context context) {
        contentTemp = new LinearLayout(context);
        contentTemp.setWeightSum(2);
        contentTemp.setId(ViewCompat.generateViewId());
        contentTemp.setOrientation(LinearLayout.HORIZONTAL);
        contentTemp.addView(textTempMin);
        contentTemp.addView(textTempMax);

        final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        lp.gravity = Gravity.CENTER;
        lp.topMargin = UtilsConvert.convertDpToPixel(10);
        contentTemp.setLayoutParams(lp);
    }

    private void createContentDetails(Context context) {
        final int margin = UtilsConvert.convertDpToPixel(16);
        final int padding = UtilsConvert.convertDpToPixel(10);

        detailsAdapter = new ForecastDetailsAdapter();

        contentDetails = new RecyclerView(context);
        contentDetails.setId(ViewCompat.generateViewId());
        contentDetails.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_content));
        contentDetails.setPadding(padding, padding, padding, padding);
        contentDetails.setClipToPadding(false);
        contentDetails.setAlpha(0f);
        contentDetails.setScaleY(0f);

        contentDetails.setLayoutManager(new GridLayoutManager(context, 2));
        contentDetails.setAdapter(detailsAdapter);

        final RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lp.height = UtilsConvert.convertDpToPixel(200);
        lp.bottomMargin = margin;
        lp.leftMargin = margin;
        lp.rightMargin = margin;
        contentDetails.setLayoutParams(lp);
    }

    private void createContent(Context context) {
        content = new LinearLayout(context);
        content.setId(ViewCompat.generateViewId());
        content.setOrientation(LinearLayout.VERTICAL);
        content.addView(textDay);
        content.addView(animationWeather);
        content.addView(textTemp);
        content.addView(contentTemp);

        final NestedScrollView.LayoutParams lp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        content.setLayoutParams(lp);
    }

    private void createScroller(Context context) {
        final int padding = UtilsConvert.convertDpToPixel(20);
        scroller = new NestedScrollView(context);
        scroller.setId(ViewCompat.generateViewId());
        //scroller.setFillViewport(true);
        scroller.addView(content);
        scroller.setPadding(padding, padding, padding, padding);
        scroller.setClipToPadding(false);

        final RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        lp.addRule(RelativeLayout.ABOVE, contentDetails.getId());
        scroller.setLayoutParams(lp);
    }

    private void createContainer(Context context) {
        container = new RelativeLayout(context);
        container.setBackground(createBackground(context));
        container.setElevation(5f);
        container.setId(ViewCompat.generateViewId());
        container.addView(contentDetails);
        container.addView(scroller);

        final CoordinatorLayout.LayoutParams lp = new CoordinatorLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );

        lp.height = rectModel.getViewHeight();
        lp.width = rectModel.getViewWidth();
        container.setLayoutParams(lp);

        container.setX(rectModel.getAxisX());
        container.setY(rectModel.getAxisY());
    }

    private void createIconClose(Context context) {
        final int padding = UtilsConvert.convertDpToPixel(20);
        iconClose = new ImageView(context);
        iconClose.setImageResource(R.drawable.ic_close);
        iconClose.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_icon));
        iconClose.setPadding(padding, padding, padding, padding);
        iconClose.setElevation(5f);
        iconClose.setVisibility(INVISIBLE);

        final CoordinatorLayout.LayoutParams lp = new CoordinatorLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        lp.height = UtilsConvert.convertDpToPixel(60);
        lp.width = UtilsConvert.convertDpToPixel(60);
        lp.setAnchorId(container.getId());
        lp.anchorGravity = Gravity.CENTER_HORIZONTAL;
        iconClose.setLayoutParams(lp);
    }

    private void init() {
        addView(container);
        addView(iconClose);

        setClickable(true);
        setFocusable(true);

        final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );
        lp.height = FrameLayout.LayoutParams.MATCH_PARENT;
        lp.width = FrameLayout.LayoutParams.MATCH_PARENT;
        setLayoutParams(lp);
    }

    private Drawable createBackground(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.bg_card_details);
    }

    private void animateIcon(boolean show) {
        iconClose.setVisibility(VISIBLE);
        final Animation myAnim = AnimationUtils.loadAnimation(getContext(),
                show ? R.anim.scale_in : R.anim.scale_out);
        BounceInterpolator interpolator = new BounceInterpolator(0.1, 15);
        myAnim.setInterpolator(interpolator);
        iconClose.startAnimation(myAnim);
    }

    public void setCurrentModel(WeatherCurrentModel currentModel) {
        this.currentModel = currentModel;

        if (currentModel != null) {
            final Weather weather = UtilsList.getFirst(currentModel.getWeather());
            final long date = currentModel.getDt() * 1000L;
            final String dayName = UtilsDate.getDayName(date);

            if (dayName != null) {
                textDay.setText(dayName);
            }

            textTemp.setText(UtilsApp.getTempFormat(currentModel.getMain().getTemp()));
            textTempMin.setText(UtilsApp.getTempFormat(currentModel.getMain().getTempMin()));
            textTempMax.setText(UtilsApp.getTempFormat(currentModel.getMain().getTempMax()));
            if (weather != null) {
                animationWeather.setAnimation(UtilsApp.getWeatherAnimation(weather.getId()));
                animationWeather.setRepeatCount(ValueAnimator.INFINITE);
                animationWeather.playAnimation();
            }

            UtilsApp.setBackgroundCard(
                    container.getBackground(),
                    date
            );

            contentDetails.animate().scaleY(1f).alpha(1f)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            bindContentDetails(currentModel);
                        }
                    })
                    .setDuration(500)
                    .start();
        }
    }

    public void setDaily(Daily daily) {
        this.daily = daily;

        if (daily != null) {
            final Weather weather = UtilsList.getFirst(daily.getWeather());
            final long date = daily.getDt() * 1000L;
            final String dayName = UtilsDate.getDayName(date);

            if (dayName != null) {
                textDay.setText(dayName);
            }

            textTemp.setText(UtilsApp.getTempFormat(daily.getTemp().getDay()));
            textTempMin.setText(UtilsApp.getTempFormat(daily.getTemp().getMin()));
            textTempMax.setText(UtilsApp.getTempFormat(daily.getTemp().getMax()));
            if (weather != null) {
                animationWeather.setAnimation(UtilsApp.getWeatherAnimation(weather.getId()));
                animationWeather.setRepeatCount(ValueAnimator.INFINITE);
                animationWeather.playAnimation();
            }

            UtilsApp.setBackgroundCard(
                    container.getBackground(),
                    date
            );

            contentDetails.animate().scaleY(1f).alpha(1f)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            bindContentDetails(daily);
                        }
                    })
                    .setDuration(500)
                    .start();
        }
    }

    public Daily getDaily() {
        return daily;
    }

    public WeatherCurrentModel getCurrentModel() {
        return currentModel;
    }

    private void bindContentDetails(Daily daily) {
        final List<DetailsModel> detailsModels = new ArrayList<>();
        final long date = daily.getDt() * 1000L;
        detailsModels.add(new DetailsModel(
                "Date", UtilsDate.getStringDate(date)
        ));
        detailsModels.add(new DetailsModel(
                "Sunrise", UtilsDate.getTime(daily.getSunrise() * 1000L)
        ));
        detailsModels.add(new DetailsModel(
                "Sunset", UtilsDate.getTime(daily.getSunset() * 1000L)
        ));
        detailsModels.add(new DetailsModel(
                "Moonrise", UtilsDate.getTime(daily.getMoonrise() * 1000L)
        ));
        detailsModels.add(new DetailsModel(
                "Pressure", String.format("%s mmHg", daily.getPressure())
        ));
        detailsModels.add(new DetailsModel(
                "Humidity", String.format("%s %%", daily.getHumidity())
        ));
        detailsModels.add(new DetailsModel(
                "Dew Point", UtilsApp.getCelciusFormat(daily.getDewPoint())
        ));
        detailsModels.add(new DetailsModel(
                "Windchill", UtilsApp.getCelciusFormat(daily.getWindDeg())
        ));
        detailsModels.add(new DetailsModel(
                "Wind speed", String.format("%s m/s", daily.getWindSpeed())
        ));

        detailsAdapter.setModels(detailsModels);
        detailsAdapter.notifyDataSetChanged();
    }

    private void bindContentDetails(WeatherCurrentModel model) {
        final List<DetailsModel> detailsModels = new ArrayList<>();
        final Weather weather = UtilsList.getFirst(model.getWeather());
        final long date = model.getDt() * 1000L;
        detailsModels.add(new DetailsModel(
                "Date", UtilsDate.getStringDate(date)
        ));
        if (weather != null) {
            detailsModels.add(new DetailsModel(
                    "Weather", String.format("%s (%s)", weather.getMain(), weather.getDescription())
            ));
        }
        detailsModels.add(new DetailsModel(
                "Pressure", String.format("%s mmHg", model.getMain().getPressure())
        ));
        detailsModels.add(new DetailsModel(
                "Humidity", String.format("%s %%", model.getMain().getHumidity())
        ));
        detailsModels.add(new DetailsModel(
                "Windchill", UtilsApp.getCelciusFormat(model.getWind().getDeg())
        ));
        detailsModels.add(new DetailsModel(
                "Wind speed", String.format("%s m/s", model.getWind().getSpeed())
        ));

        detailsAdapter.setModels(detailsModels);
        detailsAdapter.notifyDataSetChanged();
    }

    public void startAnimationEnter() {
        if (isAnimationEnterRun) {
            return;
        }
        isAnimationEnterRun = true;

        final int maxWidth = UtilsConvert.getScreenWidth();
        final int screenHeight = UtilsConvert.getScreenHeight();
        final int maxHeight = screenHeight - UtilsConvert.convertDpToPixel(75);
        final float toY = screenHeight - maxHeight;
        final float toX = 0;

        final AnimatorSet animatorSet = new AnimatorSet();

        final ValueAnimator animatorWidth = ValueAnimator.ofFloat(rectModel.getViewWidth(), maxWidth);
        animatorWidth.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                final CoordinatorLayout.LayoutParams lp = (LayoutParams) container.getLayoutParams();
                final float value = (float)animation.getAnimatedValue();

                lp.width = (int) value;
                container.requestLayout();
            }
        });

        final ValueAnimator animatorHeight = ValueAnimator.ofFloat(rectModel.getViewHeight(), maxHeight);
        animatorHeight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                final CoordinatorLayout.LayoutParams lp = (LayoutParams) container.getLayoutParams();
                final float value = (float)animation.getAnimatedValue();

                lp.height = (int) value;
                container.requestLayout();
            }
        });

        final ValueAnimator animatorY = ValueAnimator.ofFloat(rectModel.getAxisY(), toY);
        animatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float value = (float)animation.getAnimatedValue();
                container.setY(value);
            }
        });

        final ValueAnimator animatorX = ValueAnimator.ofFloat(rectModel.getAxisX(), toX);
        animatorX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float value = (float)animation.getAnimatedValue();
                container.setX(value);
            }
        });

        final ValueAnimator animatorAlpha = ValueAnimator.ofFloat(0f, 1f);
        animatorAlpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float value = (float)animation.getAnimatedValue();
                content.setAlpha(value);
            }
        });

        animatorSet.playTogether(animatorWidth, animatorHeight, animatorY, animatorX, animatorAlpha);
        animatorSet.setDuration(ANIMATION_ENTER_DURATION);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animateIcon(true);
                isAnimationEnterRun = false;
            }
        });
        animatorSet.start();
    }

    public void startAnimationExit() {
        if (isAnimationExitRun) {
            return;
        }
        isAnimationExitRun = true;

        final int currentWidth = container.getMeasuredWidth();
        final int currentHeight = container.getMeasuredHeight();
        final float currentY = container.getY();
        final float currentX = container.getX();

        final AnimatorSet animatorSet = new AnimatorSet();

        final ValueAnimator animatorWidth = ValueAnimator.ofFloat(currentWidth, rectModel.getViewWidth());
        animatorWidth.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final CoordinatorLayout.LayoutParams lp = (LayoutParams) container.getLayoutParams();
                final float value = (float)animation.getAnimatedValue();

                lp.width = (int) value;
                container.requestLayout();
            }
        });

        final ValueAnimator animatorHeight = ValueAnimator.ofFloat(currentHeight, rectModel.getViewHeight());
        animatorHeight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final CoordinatorLayout.LayoutParams lp = (LayoutParams) container.getLayoutParams();
                final float value = (float)animation.getAnimatedValue();

                lp.height = (int) value;
                container.requestLayout();
            }
        });

        final ValueAnimator animatorY = ValueAnimator.ofFloat(currentY, rectModel.getAxisY());
        animatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float value = (float)animation.getAnimatedValue();
                container.setY(value);
            }
        });

        final ValueAnimator animatorX = ValueAnimator.ofFloat(currentX, rectModel.getAxisX());
        animatorX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float value = (float)animation.getAnimatedValue();
                container.setX(value);
            }
        });

        final ValueAnimator animatorAlpha = ValueAnimator.ofFloat(1f, 0f);
        animatorAlpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float value = (float)animation.getAnimatedValue();
                content.setAlpha(value);
            }
        });

        animatorSet.playTogether(animatorWidth, animatorHeight, animatorY, animatorX, animatorAlpha);
        animatorSet.setDuration(ANIMATION_EXIT_DURATION);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                contentDetails.setVisibility(GONE);
                animateIcon(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ViewParent parent = getParent();
                if (parent instanceof ViewGroup) {
                    ((ViewGroup)parent).removeView(WeatherDetailView.this);
                }
                isAnimationExitRun = false;
            }
        });
        animatorSet.start();
    }
}

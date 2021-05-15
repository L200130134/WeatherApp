package com.rikyahmadfathoni.test.weatherapp.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.core.content.ContextCompat;
import androidx.core.os.ConfigurationCompat;

import com.rikyahmadfathoni.test.weatherapp.Common;
import com.rikyahmadfathoni.test.weatherapp.R;
import com.rikyahmadfathoni.test.weatherapp.base.BaseApp;

import java.util.Locale;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;

public class UtilsApp {

    public static void setBackgroundCard(Drawable drawable, long timestamp) {
        setBackgroundCard(drawable, UtilsApp.getBackgroundColor(timestamp));
    }

    public static void setBackgroundCard(Drawable drawable, @ColorRes int color) {
        if (drawable instanceof RippleDrawable) {
            final RippleDrawable rd = (RippleDrawable) drawable;
            final Drawable cd = rd.getDrawable(0);
            if (cd instanceof GradientDrawable) {
                final GradientDrawable gd = (GradientDrawable) cd;
                gd.setColor(ContextCompat.getColor(BaseApp.getInstance(), color));
            }
        } else if (drawable instanceof GradientDrawable) {
            final GradientDrawable gd = (GradientDrawable) drawable;
            gd.setColor(ContextCompat.getColor(BaseApp.getInstance(), color));
        }
    }

    @ColorRes
    public static int getBackgroundColor(long time) {
        return getBackgroundColorByPosition(UtilsDate.getDayOfWeek(time));
    }

    @ColorRes
    public static int getBackgroundColorByPosition(int position) {
        if (position > 7) {
            position -= 7;
        }
        switch (position) {
            case 1:
                return R.color.card_violet;
            case 2:
                return R.color.card_green;
            case 3:
                return R.color.card_pink;
            case 4:
                return R.color.card_orange;
            case 5:
                return R.color.card_blue;
            case 6:
                return R.color.card_red;
            case 7:
                return R.color.card_green_dark;
        }
        return R.color.card_blue;
    }

    @NonNull
    public static String getTempFormat(Double temp) {
        if (temp == null) {
            temp = 0d;
        }
        return String.format("%s°", Math.round(temp));
    }

    @NonNull
    public static String getCelciusFormat(Double temp) {
        if (temp == null) {
            temp = 0d;
        }
        return String.format("%s°C", Math.round(temp));
    }

    /**
     * Check current direction of application. if is RTL return true
     *
     * @param context instance of {@link Context}
     * @return boolean value
     */
    public static boolean isRTL(Context context) {
        Locale locale = ConfigurationCompat.getLocales(context.getResources().getConfiguration()).get(0);
        final int directionality = Character.getDirectionality(locale.getDisplayName().charAt(0));
        return directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT ||
                directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC;
    }

    /**
     * Get weather status string according to weather status code
     *
     * @param weatherCode weather status code
     * @return String weather status
     */
    public static String getWeatherStatus(int weatherCode) {
        if (weatherCode / 100 == 2) {
            return Common.WEATHER_STATUS[0];
        } else if (weatherCode / 100 == 3) {
            return Common.WEATHER_STATUS[1];
        } else if (weatherCode / 100 == 5) {
            return Common.WEATHER_STATUS[2];
        } else if (weatherCode / 100 == 6) {
            return Common.WEATHER_STATUS[3];
        } else if (weatherCode / 100 == 7) {
            return Common.WEATHER_STATUS[4];
        } else if (weatherCode == 800) {
            return Common.WEATHER_STATUS[5];
        } else if (weatherCode == 801) {
            return Common.WEATHER_STATUS[6];
        } else if (weatherCode == 803) {
            return Common.WEATHER_STATUS[7];
        } else if (weatherCode / 100 == 8) {
            return Common.WEATHER_STATUS[8];
        }
        return Common.WEATHER_STATUS[4];
    }

    /**
     * Get animation file according to weather status code
     *
     * @param weatherCode int weather status code
     * @return id of animation json file
     */
    public static int getWeatherAnimation(int weatherCode) {
        if (weatherCode / 100 == 2) {
            return R.raw.storm_weather;
        } else if (weatherCode / 100 == 3) {
            return R.raw.rainy_weather;
        } else if (weatherCode / 100 == 5) {
            return R.raw.rainy_weather;
        } else if (weatherCode / 100 == 6) {
            return R.raw.snow_weather;
        } else if (weatherCode / 100 == 7) {
            return R.raw.unknown;
        } else if (weatherCode == 800) {
            return R.raw.clear_day;
        } else if (weatherCode == 801) {
            return R.raw.few_clouds;
        } else if (weatherCode == 803) {
            return R.raw.broken_clouds;
        } else if (weatherCode / 100 == 8) {
            return R.raw.cloudy_weather;
        }
        return R.raw.unknown;
    }

    /**
     * If network connection is connect, return true
     *
     * @return boolean value
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static boolean isNetworkConnected() {
        NetworkInfo info = getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    /**
     * Get activity network info instace
     *
     * @return instance of {@link NetworkInfo}
     */

    @RequiresPermission(ACCESS_NETWORK_STATE)
    private static NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager cm =
                (ConnectivityManager) BaseApp.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return null;
        return cm.getActiveNetworkInfo();
    }
}

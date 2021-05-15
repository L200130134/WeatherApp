package com.rikyahmadfathoni.test.weatherapp.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Size;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import com.rikyahmadfathoni.test.weatherapp.base.BaseApp;

public class UtilsConvert {

    public static float density() {
        return Resources.getSystem().getDisplayMetrics().density;
    }

    public static int convertDpToPixel(float dp) {
        try {
            DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
            return (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    dp,
                    metrics);
        } catch (Exception e) {
            return 0;
        }
    }

    public static int convertPixelToDp(int pixel) {
        try {
            DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
            return Math.round(pixel / metrics.density);
        } catch (Exception e) {
            return 0;
        }
    }

    public static int convertSpToPixel(float sp) {
        try {
            DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
            return (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_SP,
                    sp,
                    metrics);
        } catch (Exception e) {
            return 0;
        }
    }

    public static float convertPixelToSp(int pixel) {
        try {
            DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
            return pixel / metrics.scaledDensity;
        } catch (Exception e) {
            return 0f;
        }
    }

    public static int getScreenHeight() {
        try {
            DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
            return metrics.heightPixels;
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getScreenWidth() {
        try {
            DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
            return metrics.widthPixels;
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getScreenWidth(float padding) {
        return Math.max(0, getScreenWidth() - convertDpToPixel(padding));
    }

    public static int getScreenWidthInDp() {
        try {
            DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
            return Math.round(metrics.widthPixels / metrics.density);
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getScreenHeightInDp() {
        try {
            DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
            return Math.round(metrics.heightPixels / metrics.density);
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getNavigationBarHeight(){
        int result = 0;
        Resources resources = BaseApp.getInstance().getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static Rect getWindowSize(Activity activity) {
        final Rect rectangle = new Rect();
        WindowManager windowManager = (WindowManager) BaseApp.getInstance().getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            final Display display = windowManager.getDefaultDisplay();
            display.getRectSize(rectangle);
        }
        return rectangle;
    }

    public static int getStatusBarHeight() {
        int result = 0;
        Resources resources = BaseApp.getInstance().getResources();
        int resourceId = resources.getIdentifier(
                "status_bar_height", "dimen", "android"
        );
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static Size getRealSize() {
        return getRealSize(false);
    }

    public static Size getRealSize(boolean isLandscape) {
        WindowManager windowManager =
                (WindowManager) BaseApp.getInstance().getSystemService(Context.WINDOW_SERVICE);
        int screenHeight = 0;
        int screenWidth = 0;
        if (windowManager != null) {
            final Display display = windowManager.getDefaultDisplay();
            Point outPoint = new Point();
            if (Build.VERSION.SDK_INT >= 19) {
                // include navigation bar
                display.getRealSize(outPoint);
            } else {
                // exclude navigation bar
                display.getSize(outPoint);
            }
            if (outPoint.y > outPoint.x) {
                screenHeight = outPoint.y;
                screenWidth = outPoint.x;
            } else {
                screenHeight = outPoint.x;
                screenWidth = outPoint.y;
            }
        }
        if (isLandscape) {
            return new Size(screenHeight, screenWidth);
        }
        return new Size(screenWidth, screenHeight);
    }
}

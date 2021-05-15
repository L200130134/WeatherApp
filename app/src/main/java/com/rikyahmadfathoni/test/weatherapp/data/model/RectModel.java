package com.rikyahmadfathoni.test.weatherapp.data.model;

import android.view.View;

import com.rikyahmadfathoni.test.weatherapp.util.UtilsConvert;

public class RectModel {

    private int viewWidth;
    private int viewHeight;
    private int axisX;
    private int axisY;

    public RectModel(View view) {
        if (view != null) {
            final int[] location = new int[2];
            view.getLocationOnScreen(location);

            this.viewWidth = view.getMeasuredWidth();
            this.viewHeight = view.getMeasuredHeight();
            this.axisX = location[0];
            this.axisY = location[1] - UtilsConvert.getNavigationBarHeight() + getElevation(view);
        }
    }

    private int getElevation(View view) {
        return (int) view.getElevation();
    }

    public int getViewWidth() {
        return viewWidth;
    }

    public void setViewWidth(int viewWidth) {
        this.viewWidth = viewWidth;
    }

    public int getViewHeight() {
        return viewHeight;
    }

    public void setViewHeight(int viewHeight) {
        this.viewHeight = viewHeight;
    }

    public int getAxisX() {
        return axisX;
    }

    public void setAxisX(int axisX) {
        this.axisX = axisX;
    }

    public int getAxisY() {
        return axisY;
    }

    public void setAxisY(int axisY) {
        this.axisY = axisY;
    }
}

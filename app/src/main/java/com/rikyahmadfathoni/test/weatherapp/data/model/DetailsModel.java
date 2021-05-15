package com.rikyahmadfathoni.test.weatherapp.data.model;

public class DetailsModel {

    private int icon;
    private String name;
    private String value;

    public DetailsModel() {
        super();
    }

    public DetailsModel(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public DetailsModel(int icon, String name, String value) {
        this.icon = icon;
        this.name = name;
        this.value = value;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

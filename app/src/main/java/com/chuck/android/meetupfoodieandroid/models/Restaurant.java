package com.chuck.android.meetupfoodieandroid.models;

import java.util.List;

public class Restaurant {
    private String name;
    private List<String> locations;

    public Restaurant(String name, List<String> locations) {
        this.name = name;
        this.locations = locations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }
}

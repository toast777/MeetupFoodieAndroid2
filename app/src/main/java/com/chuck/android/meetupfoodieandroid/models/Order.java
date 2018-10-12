package com.chuck.android.meetupfoodieandroid.models;

import java.util.Date;

public class Order {
    private String id;
    private String region;
    private String date;
    private int total;
    private String location;
    private String restaurant;

    public Order(String id,String region, String date, int total,String location, String restaurant)
    {
        this.id = id;
        this.region = region;
        this.date = date;
        this.total = total;
        this.location = location;
        this.restaurant = restaurant;

    }
    //Needed for firebase
    public Order(){}



    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }
}

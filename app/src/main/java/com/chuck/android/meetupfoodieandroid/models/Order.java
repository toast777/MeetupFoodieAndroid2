package com.chuck.android.meetupfoodieandroid.models;

import java.util.Date;

public class Order {
    private String id;
    private String region;
    private String date;
    private int total;

    public Order(String id,String region, String date, int total)
    {
        this.id = id;
        this.region = region;
        this.date = date;
        this.total = total;
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
}

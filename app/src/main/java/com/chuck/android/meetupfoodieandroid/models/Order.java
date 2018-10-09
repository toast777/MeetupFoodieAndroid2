package com.chuck.android.meetupfoodieandroid.models;

import java.util.Date;

public class Order {
    private String id;
    private String region;
    private Date date;
    private int total;

    public Order(String id, String region, Date date, int total)
    {
        this.id = id;
        this.region = region;
        this.date = date;
        this.total = total;
    }
    //Needed for firebase
    public Order(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}

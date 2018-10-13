package com.chuck.android.meetupfoodieandroid.models;

public class FirebaseFoodTopping {
    private String name;
    private String imgSrc;
    private double price;

    public FirebaseFoodTopping(String name, String imgSrc, double price) {
        this.name = name;
        this.imgSrc = imgSrc;
        this.price = price;
    }
    public FirebaseFoodTopping(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

package com.chuck.android.meetupfoodieandroid.models;

public class FirebaseFoodItem {
    private String name;
    private String imgSrc;
    private double price;
    private boolean allowAddOns;
    private int numAddOns;


    public FirebaseFoodItem( String name, String imgSrc, double price,boolean allowAddOns,int numAddOns) {
        this.name = name;
        this.imgSrc = imgSrc;
        this.price = price;
        this.allowAddOns = allowAddOns;
        this.numAddOns = numAddOns;
    }
    public FirebaseFoodItem(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAllowAddOns() {
        return allowAddOns;
    }

    public void setAllowAddOns(boolean allowAddOns) {
        this.allowAddOns = allowAddOns;
    }

    public int getNumAddOns() {
        return numAddOns;
    }

    public void setNumAddOns(int numAddOns) {
        this.numAddOns = numAddOns;
    }
}

package com.chuck.android.meetupfoodieandroid.models;

import android.os.Parcel;
import android.os.Parcelable;

public class FirebaseFoodTopping implements Parcelable{
    private String toppingName;
    private String imgSrc;
    private double price;

    public FirebaseFoodTopping(String name, String imgSrc, double price) {
        this.toppingName = name;
        this.imgSrc = imgSrc;
        this.price = price;
    }
    public FirebaseFoodTopping(){}

    public String getToppingName() {
        return toppingName;
    }

    public void setToppingName(String name) {
        this.toppingName = name;
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

    protected FirebaseFoodTopping(Parcel in) {
        toppingName = in.readString();
        imgSrc = in.readString();
        price = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(toppingName);
        dest.writeString(imgSrc);
        dest.writeDouble(price);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FirebaseFoodTopping> CREATOR = new Parcelable.Creator<FirebaseFoodTopping>() {
        @Override
        public FirebaseFoodTopping createFromParcel(Parcel in) {
            return new FirebaseFoodTopping(in);
        }

        @Override
        public FirebaseFoodTopping[] newArray(int size) {
            return new FirebaseFoodTopping[size];
        }
    };
}

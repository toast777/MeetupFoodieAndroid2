package com.chuck.android.meetupfoodieandroid.models;

import android.os.Parcel;
import android.os.Parcelable;

public class FirebaseFoodItem implements Parcelable {
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

    //Parcelable Stuff - from http://www.parcelabler.com/
    @Override
    public int describeContents() {
        return 0;
    }

    protected FirebaseFoodItem(Parcel in) {
        name = in.readString();
        imgSrc = in.readString();
        price = in.readDouble();
        allowAddOns = in.readByte() != 0x00;
        numAddOns = in.readInt();
    }
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(imgSrc);
        parcel.writeDouble(price);
        parcel.writeByte((byte) (allowAddOns ? 0x01 : 0x00));
        parcel.writeInt(numAddOns);
    }
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FirebaseFoodItem> CREATOR = new Parcelable.Creator<FirebaseFoodItem>() {
        @Override
        public FirebaseFoodItem createFromParcel(Parcel in) {
            return new FirebaseFoodItem(in);
        }

        @Override
        public FirebaseFoodItem[] newArray(int size) {
            return new FirebaseFoodItem[size];
        }
    };
}

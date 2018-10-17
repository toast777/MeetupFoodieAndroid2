package com.chuck.android.meetupfoodieandroid.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class CustomFoodItem implements Parcelable{
    private FirebaseFoodItem foodItem;
    private String id;
    private Double customPrice;
    private List<FirebaseFoodTopping> toppings;


    public CustomFoodItem(FirebaseFoodItem foodItem, Double customPrice, List<FirebaseFoodTopping> toppings,String id) {
        this.foodItem = foodItem;
        this.customPrice = customPrice;
        this.toppings = toppings;
        this.id = id;
    }
    public CustomFoodItem() {}

    public FirebaseFoodItem getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(FirebaseFoodItem foodItem) {
        this.foodItem = foodItem;
    }

    public Double getCustomPrice() {
        return customPrice;
    }

    public void setCustomPrice(Double customPrice) {
        this.customPrice = customPrice;
    }

    public List<FirebaseFoodTopping> getToppings() {
        return toppings;
    }

    public void setToppings(List<FirebaseFoodTopping> toppings) {
        this.toppings = toppings;
    }
    protected CustomFoodItem(Parcel in) {
        foodItem = (FirebaseFoodItem) in.readValue(FirebaseFoodItem.class.getClassLoader());
        customPrice = in.readDouble();
        id = in.readString();
        if (in.readByte() == 0x01) {
            toppings = new ArrayList<FirebaseFoodTopping>();
            in.readList(toppings, FirebaseFoodTopping.class.getClassLoader());
        } else {
            toppings = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(foodItem);
        dest.writeDouble(customPrice);
        dest.writeString(id);
        if (toppings == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(toppings);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CustomFoodItem> CREATOR = new Parcelable.Creator<CustomFoodItem>() {
        @Override
        public CustomFoodItem createFromParcel(Parcel in) {
            return new CustomFoodItem(in);
        }

        @Override
        public CustomFoodItem[] newArray(int size) {
            return new CustomFoodItem[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

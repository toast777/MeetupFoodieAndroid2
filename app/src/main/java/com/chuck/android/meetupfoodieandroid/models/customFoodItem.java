package com.chuck.android.meetupfoodieandroid.models;

import java.util.List;

public class customFoodItem {
    private FirebaseFoodItem foodItem;
    private double customPrice;
    private List<FirebaseFoodTopping> toppings;


    public customFoodItem(FirebaseFoodItem foodItem, double customPrice, List<FirebaseFoodTopping> toppings) {
        this.foodItem = foodItem;
        this.customPrice = customPrice;
        this.toppings = toppings;
    }

    public FirebaseFoodItem getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(FirebaseFoodItem foodItem) {
        this.foodItem = foodItem;
    }

    public double getCustomPrice() {
        return customPrice;
    }

    public void setCustomPrice(double customPrice) {
        this.customPrice = customPrice;
    }

    public List<FirebaseFoodTopping> getToppings() {
        return toppings;
    }

    public void setToppings(List<FirebaseFoodTopping> toppings) {
        this.toppings = toppings;
    }
}

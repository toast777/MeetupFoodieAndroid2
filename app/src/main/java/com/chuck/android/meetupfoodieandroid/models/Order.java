package com.chuck.android.meetupfoodieandroid.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
    private String id;
    private String region;
    private String date;
    private Double total;
    private String location;
    private String restaurant;
    private List<CustomFoodItem> foodItems;

    public Order(String id,String region, String date, Double total,String location, String restaurant, List<CustomFoodItem> foodItems)
    {
        this.id = id;
        this.region = region;
        this.date = date;
        this.total = total;
        this.location = location;
        this.restaurant = restaurant;
        this.foodItems = foodItems;
    }
    //Simple Constructor
    public Order(String id,String region,String date)
    {
        this.id = id;
        this.region = region;
        this.date = date;
        total = 0.00;
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

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
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

    public List<CustomFoodItem> getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(List<CustomFoodItem> foodItems) {
        this.foodItems = foodItems;
    }
    public void addFoodItems(CustomFoodItem foodItem){
        this.foodItems.add(foodItem);
    }
    public void deleteFoodItems(CustomFoodItem foodItem){
        this.foodItems.clear();
    }
}

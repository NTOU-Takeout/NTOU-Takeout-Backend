package com.ntoutakeout.backend.entity;

import java.util.ArrayList;
import java.util.List;

public class Dish {
    private String name;
    private String description;
    private String picture;
    private int price;
    private String category;
    private int salesVolume;
    private List<DishAttribute> dishAttributes;

    public Dish() {
        dishAttributes = new ArrayList<DishAttribute>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() { return picture; }

    public void setPicture(String picture) { this.picture = picture; }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(int salesVolume) {
        this.salesVolume = salesVolume;
    }

    public List<DishAttribute> getDishAttributes() {
        return dishAttributes;
    }

    public void setDishAttributes(List<DishAttribute> dishAttributes) {
        this.dishAttributes = dishAttributes;
    }

    @Override
    public String toString() {
        return "Name: " + name +
                "\nDescription: " + description +
                "\nPrice: " + price +
                "\nCategory: " + category +
                "\nSalesVolume: " + salesVolume +
                "\nDishAttributes: " + dishAttributes;
    }
}

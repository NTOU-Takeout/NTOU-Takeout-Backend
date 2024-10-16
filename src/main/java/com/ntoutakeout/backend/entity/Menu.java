package com.ntoutakeout.backend.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "menu")
public class Menu {
    @Id
    private String id;
    private String storeId;
    private List<String> categories;
    private List<Dish> dishes;

    public Menu() {
        categories = new ArrayList<>();
        dishes = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<Dish> getDishes() {return dishes;}

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    @Override
    public String toString() {
        return "Id: " + id +
                "\nStoreId: " + storeId +
                "\nCategories: " + categories +
                "\nDishes: " + dishes;
    }
}
package com.ntoutakeout.backend.entity;

import java.util.ArrayList;
import java.util.List;

public class Dish {
    private String id;
    private String menuId;
    private String name;
    private String description;
    private Double price;
    private String category;
    private Integer salesVolume;
    private List<DishAttribute> dishAttributes;

    public Dish() {
        dishAttributes = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(Integer salesVolume) {
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
        return "Dish{" +
                "id='" + id + '\'' +
                ", menuId='" + menuId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", salesVolume=" + salesVolume +
                ", dishAttributes=" + dishAttributes +
                '}';
    }
}
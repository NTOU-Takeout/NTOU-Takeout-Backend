package com.ntoutakeout.backend.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "menu")
public class Menu {
    @Id
    private String id;
    private String storeId;
    private List<Pair<String, List<String>>> categories;

    public Menu() {
        categories = new ArrayList<>();
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

    public List<Pair<String, List<String>>> getCategories() {
        return categories;
    }

    public void setCategories(List<Pair<String, List<String>>> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id='" + id + '\'' +
                ", storeId='" + storeId + '\'' +
                ", categories=" + categories +
                '}';
    }
}
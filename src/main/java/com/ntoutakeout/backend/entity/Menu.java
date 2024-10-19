package com.ntoutakeout.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
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
}

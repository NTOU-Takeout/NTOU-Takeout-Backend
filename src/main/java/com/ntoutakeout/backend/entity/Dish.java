package com.ntoutakeout.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Dish {
    private String name;
    private String description;
    private String picture;
    private Double price;
    private String category;
    private Integer salesVolume;
    private List<DishAttribute> dishAttributes;

    public Dish() {
        dishAttributes = new ArrayList<DishAttribute>();
    }
}

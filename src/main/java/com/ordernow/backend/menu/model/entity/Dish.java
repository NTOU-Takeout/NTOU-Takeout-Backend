package com.ordernow.backend.menu.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Document(collection = "dish")
public class Dish {
    @Id
    private String id = "";
    private String name;
    private String description;
    private String picture;
    private Double price;
    private String category;
    private Integer salesVolume;
    private List<DishAttribute> dishAttributes;

    public Dish() {
        dishAttributes = new ArrayList<>();
    }
}

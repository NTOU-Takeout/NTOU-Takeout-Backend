package com.ntoutakeout.backend.entity.order;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class OrderedDish {
    private String id;
    private String dishId;
    private String dishName;
    private Double price;
    private Integer quantity;
    private String note;
    private List<ChosenAttribute> chosenAttributes;


    public OrderedDish() {
        this.id = UUID.randomUUID().toString();
        this.chosenAttributes = new ArrayList<>();
    }
}

package com.ntoutakeout.backend.entity.order;

import org.springframework.data.annotation.Id;

import java.util.List;

public class OrderedDish {
    @Id
    private String dishId;
    private String dishName;
    private String price;
    private Integer quantity;
    private String note;
    private List<ChosenAttribute> chosenAttributes;
}

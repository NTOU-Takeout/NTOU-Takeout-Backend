package com.ntoutakeout.backend.entity.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Data
@AllArgsConstructor
public class OrderedDish {
    private String dishId;
    private String dishName;
    private Double price;
    private Integer quantity;
    private String note;
    private List<ChosenAttribute> chosenAttributes;


    public OrderedDish() {
        chosenAttributes = new ArrayList<>();
    }
}

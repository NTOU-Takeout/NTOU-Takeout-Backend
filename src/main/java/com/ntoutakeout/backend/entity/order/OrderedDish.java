package com.ntoutakeout.backend.entity.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
public class OrderedDish {
    private String dishId;
    private String dishName;
    private Double price;
    private Integer quantity;
    private String note;
    private List<ChosenAttribute> chosenAttributes;


}

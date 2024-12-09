package com.ordernow.backend.order.model.dto;

import com.ordernow.backend.order.model.entity.ChosenAttribute;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderedDishRequest {
    private String dishId;
    private String storeId;
    private Integer quantity;
    private String note;
    private List<ChosenAttribute> chosenAttributes;

    public OrderedDishRequest() {
        chosenAttributes = new ArrayList<>();
    }
}

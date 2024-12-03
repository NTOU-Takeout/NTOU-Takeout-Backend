package com.ordernow.backend.order.model.dto;

import com.ordernow.backend.order.model.entity.ChosenAttribute;
import lombok.Data;

import java.util.List;

@Data
public class OrderedDishPatchRequest {
    private Integer quantity;
    private String note;
    private List<ChosenAttribute> chosenAttributes;
}

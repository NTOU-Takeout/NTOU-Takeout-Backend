package com.ntoutakeout.backend.dto.order;

import com.ntoutakeout.backend.entity.order.ChosenAttribute;
import lombok.Data;

import java.util.List;

@Data
public class OrderedDishPatchRequest {
    private Integer quantity;
    private String note;
    private List<ChosenAttribute> chosenAttributes;
}

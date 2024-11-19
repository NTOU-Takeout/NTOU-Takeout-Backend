package com.ntoutakeout.backend.entity.order;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChosenAttribute {
    private String attributeName;
    private String chosenOption;
    private Double extraCost;
}

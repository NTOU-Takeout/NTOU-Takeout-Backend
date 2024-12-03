package com.ordernow.backend.entity.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChosenAttribute {
    private String attributeName;
    private String chosenOption;
    private Double extraCost;
}

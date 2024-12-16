package com.ordernow.backend.menu.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttributeOption {
    private String name;
    private Double extraCost;
    private Boolean isChosen;
}

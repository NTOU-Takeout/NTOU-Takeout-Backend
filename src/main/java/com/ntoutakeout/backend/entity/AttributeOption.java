package com.ntoutakeout.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttributeOption {
    private String name;
    private double extraCost;
    private Boolean isChosen;
}

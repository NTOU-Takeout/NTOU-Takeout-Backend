package com.ntoutakeout.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class DishAttribute {
    private String name;
    private String description;
    private String type;
    private Boolean isRequired;
    private List<AttributeOption> attributeOptions;

    public DishAttribute() {
        attributeOptions = new ArrayList<>();
    }
}

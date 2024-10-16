package com.ntoutakeout.backend.entity;

import java.util.ArrayList;
import java.util.List;

public class DishAttribute {
    private String name;
    private String description;
    private String type;
    private Boolean isRequired;
    private List<AttributeOption> attributeOptions;

    public DishAttribute() {
        attributeOptions = new ArrayList<AttributeOption>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getRequired() {
        return isRequired;
    }

    public void setRequired(Boolean required) {
        isRequired = required;
    }

    public List<AttributeOption> getAttributeOptions() {
        return attributeOptions;
    }

    public void setAttributeOptions(List<AttributeOption> attributeOptions) {
        this.attributeOptions = attributeOptions;
    }

    @Override
    public String toString() {
        return "Name: " + name +
                ", Description: " + description +
                ", Type: " + type +
                ", Required: " + isRequired;
    }
}

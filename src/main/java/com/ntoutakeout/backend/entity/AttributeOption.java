package com.ntoutakeout.backend.entity;

public class AttributeOption {
    private String name;
    private Double extraCost;
    private Boolean isChosen;

    public AttributeOption() {}

    public AttributeOption(String name, Double extraCost, Boolean isChosen) {
        this.name = name;
        this.extraCost = extraCost;
        this.isChosen = isChosen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getExtraCost() {
        return extraCost;
    }

    public void setExtraCost(Double extraCost) {
        this.extraCost = extraCost;
    }

    public Boolean getIsChosen() {
        return isChosen;
    }

    public void setIsChosen(Boolean isChosen) {
        this.isChosen = isChosen;
    }

    @Override
    public String toString() {
        return "AttributeOption{" +
                "name='" + name + '\'' +
                ", extraCost=" + extraCost +
                ", isChosen=" + isChosen +
                '}';
    }
}
package com.ntoutakeout.backend.entity;

public class AttributeOption {
    private String name;
    private int extraCost;
    private Boolean isChosen;

    public AttributeOption() {}
    public AttributeOption(String name, int extraCost, Boolean isChosen) {
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

    public int getExtraCost() {
        return extraCost;
    }

    public void setExtraCost(int extraCost) {
        this.extraCost = extraCost;
    }

    public Boolean getChosen() {
        return isChosen;
    }

    public void setChosen(Boolean chosen) {
        isChosen = chosen;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", ExtraCost: " + extraCost + ", IsChosen: " + isChosen;
    }
}

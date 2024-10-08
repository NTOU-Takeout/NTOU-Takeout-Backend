package com.ntoutakeout.backend.entity;

import java.util.ArrayList;

public class Store {
    private String id;  // Store ID
    private String name;
    private String address;
    private double rank;
    private double averagePrice;
    private ArrayList<Dish> menu;

    public Store(String name, String address, double rank, double averagePrice, ArrayList<Dish> menu) {
        this.name = name;
        this.address = address;
        this.rank = rank;
        this.averagePrice = averagePrice;
        this.menu = menu;
    }

    public Store(String name, String address, double rank, double averagePrice) {
        this.name = name;
        this.address = address;
        this.rank = rank;
        this.averagePrice = averagePrice;
        menu = new ArrayList<Dish>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getRank() {
        return rank;
    }

    public void setRank(double rank) {
        this.rank = rank;
    }

    public double getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(double averagePrice) {
        this.averagePrice = averagePrice;
    }

    public ArrayList<Dish> getMenu() {
        return menu;
    }

    public void setMenu(ArrayList<Dish> menu) {
        this.menu = menu;
    }

    @Override
    public String toString() {
        return "Name: " + name +
                "\nAddress: " + address +
                "\nRank: " + rank +
                "\nAverage Price: " + averagePrice +
                "\nMenu: " + menu;
    }
}

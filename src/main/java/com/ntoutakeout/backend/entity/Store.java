package com.ntoutakeout.backend.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Store")
public class Store {
    @Id
    private String id;
    private String name;
    private String address;
    private double rank;
    private double averagePrice;
    private Menu menu;

    public Store() {}

    public Store(String name, String address, double rank, double averagePrice, Menu menu) {
        this.name = name;
        this.address = address;
        this.rank = rank;
        this.averagePrice = averagePrice;
        this.menu = menu;
    }

    public String getId() { return id; }

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

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
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

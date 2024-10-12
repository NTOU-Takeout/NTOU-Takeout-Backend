package com.ntoutakeout.backend.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.util.Pair;

import java.util.Arrays;
import java.util.Date;

@Document(collection = "store")
public class Store {
    @Id
    private String id;
    private String name;
    private String picture;
    private String phoneNumber;
    private String address;
    private double rating;
    private double averageSpend;
    private String description;
    private Pair<Date, Date>[][] businessHours;

    public Store() {
        businessHours = new Pair[7][2];
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getAverageSpend() {
        return averageSpend;
    }

    public void setAverageSpend(double averageSpend) {
        this.averageSpend = averageSpend;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Pair<Date, Date>[][] getBusinessHours() {
        return businessHours;
    }

    public void setBusinessHours(Pair<Date, Date>[][] businessHours) {
        this.businessHours = businessHours;
    }

    @Override
    public String toString() {
        return "Id" + id +
                "\nName" + name +
                "\nPicture" + picture +
                "\nPhoneNumber" + phoneNumber +
                "\nAddress" + address +
                "\nRank" + rating +
                "\nAverageSpend" + averageSpend +
                "\nDescription" + description +
                "\nBusinessHours" + Arrays.deepToString(businessHours);
    }
}

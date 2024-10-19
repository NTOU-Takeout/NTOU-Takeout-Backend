package com.ntoutakeout.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.util.Pair;

import java.util.Date;

@Data
@AllArgsConstructor
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
}

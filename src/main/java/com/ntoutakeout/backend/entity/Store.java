package com.ntoutakeout.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.util.Pair;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    private Double rating;
    private List<String> reviewIdList;
    private String menuId;
    private Double averageSpend;
    private String description;
    private Pair<LocalTime, LocalTime>[][] businessHours;

    public Store() {
        reviewIdList = new ArrayList<>();
        businessHours = new Pair[7][2];
    }
}

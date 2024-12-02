package com.ordernow.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.util.Pair;

import java.time.LocalTime;
import java.util.ArrayList;
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

    public void setBusinessHours() {
        for (int i = 0; i < 7; i++) {
            businessHours[i][0] = Pair.of(LocalTime.of(9, 0), LocalTime.of(12, 0));
            businessHours[i][1] = Pair.of(LocalTime.of(17, 0), LocalTime.of(20, 0));
        }
    }
}

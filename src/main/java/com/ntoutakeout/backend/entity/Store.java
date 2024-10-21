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
    private Pair<LocalTime, LocalTime>[] businessHours;

    public Store() {
        reviewIdList = new ArrayList<>();
        businessHours = new Pair[7];
    }

    public void setBusinessHours(int open, int close) {
        // Loop through each day of the week (0 = Sunday, 6 = Saturday)
        for (int i = 0; i < 7; i++) {
            // Set the opening and closing times using LocalTime
            LocalTime openingTime = LocalTime.of(open, 0); // e.g., 9:00 AM
            LocalTime closingTime = LocalTime.of(close, 0); // e.g., 10:00 PM

            // Assign opening and closing times for each day
            businessHours[i] = Pair.of(openingTime, closingTime);
        }
    }
}

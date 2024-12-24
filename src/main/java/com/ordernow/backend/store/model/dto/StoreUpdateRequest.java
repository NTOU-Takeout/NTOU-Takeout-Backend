package com.ordernow.backend.store.model.dto;

import lombok.Data;
import org.springframework.data.util.Pair;

import java.time.LocalTime;

@Data
public class StoreUpdateRequest {
    private String name;
    private String picture;
    private String address;
    private String description;
    private Pair<LocalTime, LocalTime>[][] businessHours;
}

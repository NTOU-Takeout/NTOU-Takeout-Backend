package com.ordernow.backend.store.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.util.Pair;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class StoreUpdateRequest {
    private String name;
    private String picture;
    private String address;
    private String description;
    private Pair<LocalTime, LocalTime>[][] businessHours;
}

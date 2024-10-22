package com.ntoutakeout.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Document(collection = "menu")
public class Menu {
    @Id
    private String id;
    private List<Pair<String, List<String>>> categories;

    public Menu() {
        categories = new ArrayList<>();
    }
}

package com.ntoutakeout.backend.controller;

import com.ntoutakeout.backend.entity.Dishes;
import com.ntoutakeout.backend.entity.Store;
import com.ntoutakeout.backend.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController("/StoreAPI")
public class StoreController {
    @Autowired
    private StoreService storeService;
    @private static final Map<String, Store> storeMap = new HashMap<>();



    @GetMapping("/stores/{id}")
    public ResponseEntity<Store> getStore(@PathVariable("id") String storeId) {
        var store = storeMap.get(storeId);
        return store == null
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.status(HttpStatus.OK).body(store);
    }

    @GetMapping("/stores/{id}/dishes")
    public ResponseEntity<ArrayList<Dishes>> getStoreMenu(@PathVariable("id") String storeId) {
        var store = storeMap.get(storeId);
        if (store == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            var menu = store.getMenu();
            return ResponseEntity.status(HttpStatus.OK).body(menu);
        }
    }


    @GetMapping("/stores")
    public ResponseEntity<List<Store>> getStores(
            @RequestParam(value = "searchKey", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "sortField", required = false, defaultValue = "rank") String sortField,
            @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir
    ) {
        // Assuming `storeMap` contains the stores data
        List<Store> filteredStores = storeMap.values().stream()
                .filter(store -> store.getName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());

        // Determine the sorting logic based on the provided sortField and sortDir
        Comparator<Store> comparator;

        switch (sortField.toLowerCase()) {
            case "name":
                comparator = Comparator.comparing(Store::getName);
                break;
            case "averageprice":
                comparator = Comparator.comparing(Store::getAveragePrice);
                break;
            case "rank":
            default:
                comparator = Comparator.comparing(Store::getRank);
                break;
        }

        // If the sort direction is "desc", reverse the comparator
        if ("desc".equalsIgnoreCase(sortDir)) {
            comparator = comparator.reversed();
        }

        // Sort the list of stores based on the determined comparator
        filteredStores.sort(comparator);

        return ResponseEntity.status(HttpStatus.OK).body(filteredStores);
    }
}

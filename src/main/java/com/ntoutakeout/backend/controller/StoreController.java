package com.ntoutakeout.backend.controller;


import com.ntoutakeout.backend.entity.Dish;
import com.ntoutakeout.backend.entity.Store;
import com.ntoutakeout.backend.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController("/StoreAPI")
@RequestMapping(path = "/StoreAPI")
public class StoreController {
    @Autowired
    private StoreService storeService;


    @GetMapping("/getStores")
    public ResponseEntity<List<Store>> getStores(
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "sortDir", required = false) String sortDir) {
            // sortBy = "rank" / "time" / "distance" ,sortDir = "desc" / "asc"

        List<Store> storeList = storeService.getStoresFilteredAndSorted(keyword, sortBy, sortDir);

        return ResponseEntity.status(HttpStatus.OK).body(storeList);
    }

    @GetMapping("/StoreAPI/getStores/{id}/menu")
    public ResponseEntity<List<Dish>> getMenu(
            @RequestParam(value = "id", required = true) String id) {

        List<Dish> dishesList = storeService.getMenu(id);

        return ResponseEntity.status(HttpStatus.OK).body(dishesList);
    }

}

package com.ntoutakeout.backend.controller;

import com.ntoutakeout.backend.entity.Menu;
import com.ntoutakeout.backend.entity.Store;
import com.ntoutakeout.backend.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.List;

@RestController()
@RequestMapping("/api/store")
public class StoreController {
    @Autowired
    private StoreService storeService;

    // keyword: for searching keyword
    // sortBy: input 'rating', 'name'
    // sortDir: input 'desc', 'asc'
    @GetMapping("/getStores")
    public ResponseEntity<List<Store>> getStores(
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "sortBy", required = false, defaultValue = "name") String sortBy,
            @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir) {

        List<Store> storeList = storeService.getStoresFilteredAndSorted(keyword, sortBy, sortDir);
        return ResponseEntity.status(HttpStatus.OK).body(storeList);
    }

//    @GetMapping("/{storeId}/menu")
//    public ResponseEntity<Menu> getMenuByStoreId(@PathVariable String storeId) {
//        Optional<Menu> menu = storeService.getMenuById(storeId);
//        if (menu.isPresent()) {
//            return ResponseEntity.status(HttpStatus.OK).body(menu.get());
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//    }
}
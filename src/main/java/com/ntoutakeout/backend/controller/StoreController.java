package com.ntoutakeout.backend.controller;

import com.ntoutakeout.backend.entity.Menu;
import com.ntoutakeout.backend.entity.Review;
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
    // sortBy: input 'rank', 'distance', 'averagePrice', 'name'
    // sortDir: input 'desc', 'asc'
    @GetMapping("/getStores")
    public ResponseEntity<List<Store>> getStores(
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "sortBy", required = false, defaultValue = "name") String sortBy,
            @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir) {

        List<Store> storeList = storeService.getStoresFilteredAndSorted(keyword, sortBy, sortDir);
        return ResponseEntity.status(HttpStatus.OK).body(storeList);
    }

    // keyword: for searching keyword
    // sortBy: input 'rank', 'distance', 'averagePrice', 'name'
    // sortDir: input 'desc', 'asc'
    @GetMapping("/getIdList")
    public ResponseEntity<Map<String, List<String>>> getIdList(
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "sortBy", required = false, defaultValue = "name") String sortBy,
            @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir) {

        List<String> storeIdList = storeService.getStoresIdFilteredAndSorted(keyword, sortBy, sortDir);

        Map<String, List<String>> response = new HashMap<>();
        response.put("ids", storeIdList);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/getStoresByIdList")
    public ResponseEntity<Map<String, List<Store>>> getStoresByIdList(@RequestBody Map<String, List<String>> request) {
        List<String> storeIds = request.get("ids");

        List<Store> stores = storeService.getStoreListByIds(storeIds);
        Map<String, List<Store>> response = new HashMap<>();
        response.put("stores", stores);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/{storeId}/review")
    public ResponseEntity<Map<String, List<Review>>> getReviewByStoreId(@PathVariable String storeId) {


        List<Review> reviewList = storeService.getReviewById(storeId);

        Map<String, List<Review>> response = new HashMap<>();
        response.put("reviews", reviewList);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{storeId}/menu")
    public ResponseEntity<Map<String, List<Menu>>> getMenuByStoreId(@PathVariable String storeId) {

        List<Menu> dishList = storeService.getMenuById(storeId);

        Map<String, List<Menu>> response = new HashMap<>();
        response.put("menu", dishList);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
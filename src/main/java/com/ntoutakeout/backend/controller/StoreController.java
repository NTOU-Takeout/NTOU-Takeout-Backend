package com.ntoutakeout.backend.controller;

import com.ntoutakeout.backend.entity.Menu;
import com.ntoutakeout.backend.entity.Review;
import com.ntoutakeout.backend.entity.Store;
import com.ntoutakeout.backend.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@CrossOrigin
@RequestMapping("/api/store")
public class StoreController {
    @Autowired
    private StoreService storeService;

    @GetMapping("/getIdList")
    public ResponseEntity<List<String>> getIdList(
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "sortBy", required = false, defaultValue = "averageSpend") String sortBy,
            @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir) {

        List<String> storeIdList = storeService.getStoresIdFilteredAndSorted(keyword, sortBy, sortDir);

        if(storeIdList == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return ResponseEntity.status(HttpStatus.OK).body(storeIdList);
    }

    @PostMapping("/getStoresByIdList")
    public ResponseEntity<List<Store>> getStoresByIdList(@RequestBody List<String> storeIds) {
        List<Store> stores = storeService.getStoreListByIds(storeIds);
        return ResponseEntity.ok(stores);
    }


    @GetMapping("/{storeId}/review")
    public ResponseEntity<List<Review>> getReviewByStoreId(@PathVariable String storeId) {

        if (!storeService.storeExist(storeId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 if not found

        List<Review> reviewList = storeService.getReviewById(storeId);
        return ResponseEntity.status(HttpStatus.OK).body(reviewList);
    }

    @GetMapping("/{storeId}/menu")
    public ResponseEntity<List<Menu>> getMenuByStoreId(@PathVariable String storeId) {

        if (!storeService.storeExist(storeId))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 if not found

        List<Menu> dishList = storeService.getMenuById(storeId);
        return ResponseEntity.status(HttpStatus.OK).body(dishList);
    }
}
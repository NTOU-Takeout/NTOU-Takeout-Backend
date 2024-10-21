package com.ntoutakeout.backend.controller;

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

    @PostMapping("/getStoresByIds")
    public ResponseEntity<List<Store>> getStoresByIds(@RequestBody List<String> storeIds) {
        List<Store> stores = storeService.getStoreByIds(storeIds);
        return ResponseEntity.ok(stores);
    }
}
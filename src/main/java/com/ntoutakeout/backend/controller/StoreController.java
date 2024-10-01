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

import java.util.*;

@RestController("/StoreAPI")
public class StoreController {
    @Autowired
    private StoreService storeService;





    @GetMapping("/stores")
    public ResponseEntity<List<Store>> getStores(
            @RequestParam(value = "sortBy", defaultValue = "rating") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc") String order) {

        List<Store> storeList;

        // 根據排序條件選擇排序方法
        if ("time".equalsIgnoreCase(sortBy)) {
            storeList = storeService.getStoresSortedByTime();
        } else if ("rating".equalsIgnoreCase(sortBy)) {
            storeList = storeService.getStoresSortedByRating();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // 根據 `order` 決定是否需要反轉排序順序
        if ("asc".equalsIgnoreCase(order)) {
            // 如果是升序，反轉結果列表
            Collections.reverse(storeList);
        } else if (!"desc".equalsIgnoreCase(order)) {
            // 如果 `order` 不是 "asc" 或 "desc"，返回錯誤響應
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return storeList.isEmpty()
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.status(HttpStatus.OK).body(storeList);
    }

//    @GetMapping("/stores/{id}/dishes")
//    public ResponseEntity<ArrayList<Dishes>> getStoreMenu(@PathVariable("id") String storeId) {
//        var store = storeMap.get(storeId);
//        if (store == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        } else {
//            var menu = store.getMenu();
//            return ResponseEntity.status(HttpStatus.OK).body(menu);
//        }
//    }



}

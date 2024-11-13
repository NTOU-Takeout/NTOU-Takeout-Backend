package com.ntoutakeout.backend.controller;

import com.ntoutakeout.backend.entity.Store;
import com.ntoutakeout.backend.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/store")
@Slf4j
public class StoreController {
    private final StoreService storeService;
    private static final Set<String> ALLOWED_SORT_BY_FIELDS = Set.of("averageSpend", "rating", "name");
    private static final Set<String> ALLOWED_SORT_DIR_FIELDS = Set.of("asc", "desc");

    @Autowired
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/getIdList")
    public ResponseEntity<List<String>> getIdList(
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @RequestParam(value = "sortBy", defaultValue = "averageSpend") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir) {

        if(!ALLOWED_SORT_BY_FIELDS.contains(sortBy)) {
            log.error("Invalid sortBy value. Allowed values are 'averageSpend', 'rating' and 'name'.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(!ALLOWED_SORT_DIR_FIELDS.contains(sortDir)) {
            log.error("Invalid sortDir value. Allowed values are 'asc' and 'desc'.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        log.info("Fetch API: getIdList Success");
        List<String> storeIdList = storeService.getStoresIdFilteredAndSorted(keyword, sortBy, sortDir);
        return ResponseEntity.status(HttpStatus.OK).body(storeIdList);
    }

    @PostMapping("/getStoresByIds")
    public ResponseEntity<List<Store>> getStoresByIds(@RequestBody List<String> storeIds) {
        log.info("Fetch API: getStoresByIds Success");
        List<Store> stores = storeService.getStoreByIds(storeIds);
        return ResponseEntity.status(HttpStatus.OK).body(stores);
    }
}

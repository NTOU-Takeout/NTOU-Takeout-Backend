package com.ntoutakeout.backend.controller;

import com.ntoutakeout.backend.entity.Store;
import com.ntoutakeout.backend.service.StoreService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.util.List;

@RestController
@RequestMapping("/api/store")
@Slf4j
public class StoreController {
    private final StoreService storeService;

    @Autowired
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/getIdList")
    public ResponseEntity<List<String>> getIdList(
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @RequestParam(value = "sortBy", defaultValue = "averageSpend") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir,
            HttpServletRequest request) {

        try {
            storeService.validateParameters(request.getParameterMap());
            List<String> storeIdList = storeService.getStoresIdFilteredAndSorted(keyword, sortBy, sortDir);
            log.info("Fetch API: getIdList Success");
            return ResponseEntity.status(HttpStatus.OK).body(storeIdList);
        } catch (InvalidParameterException e) {
            log.error("Fetch API: getIdList Failed {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/getStoresByIds")
    public ResponseEntity<List<Store>> getStoresByIds(@RequestBody List<String> storeIds) {
        log.info("Fetch API: getStoresByIds Success");
        List<Store> stores = storeService.getStoreByIds(storeIds);
        return ResponseEntity.ok(stores);
    }
}
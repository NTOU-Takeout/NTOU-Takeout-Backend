package com.ordernow.backend.controller.v2;

import com.ordernow.backend.dto.ApiResponse;
import com.ordernow.backend.entity.Review;
import com.ordernow.backend.entity.Store;
import com.ordernow.backend.service.ReviewService;
import com.ordernow.backend.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController("StoreControllerV2")
@RequestMapping("/api/v2/stores")
@Slf4j
public class StoreController {

    private final StoreService storeService;
    private final ReviewService reviewService;
    private static final Set<String> ALLOWED_SORT_BY_FIELDS = Set.of("averageSpend", "rating", "name");
    private static final Set<String> ALLOWED_SORT_DIR_FIELDS = Set.of("asc", "desc");

    @Autowired
    public StoreController(StoreService storeService,
                           ReviewService reviewService) {
        this.storeService = storeService;
        this.reviewService = reviewService;
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<String>>> getStoreIds(
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @RequestParam(value = "sortBy", defaultValue = "rating") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "desc") String sortDir)
            throws IllegalArgumentException {

        if(!ALLOWED_SORT_BY_FIELDS.contains(sortBy)) {
            throw new IllegalArgumentException("Invalid sortBy value. Allowed values are 'averageSpend', 'rating' and 'name'.");
        }
        if(!ALLOWED_SORT_DIR_FIELDS.contains(sortDir)) {
            throw new IllegalArgumentException("Invalid sortDir value. Allowed values are 'asc' and 'desc'.");
        }

        List<String> storeIdList = storeService.getStoresIdFilteredAndSorted(keyword, sortBy, sortDir);
        ApiResponse<List<String>> apiResponse = ApiResponse.success(storeIdList);
        log.info("Get store ids successfully");
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping("/query")
    public ResponseEntity<ApiResponse<List<Store>>> getStoresByIds(
            @RequestBody List<String> storeIds) {

        List<Store> stores = storeService.getStoreByIds(storeIds);
        ApiResponse<List<Store>> apiResponse = ApiResponse.success(stores);
        log.info("Get stores successfully");
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping("/reviews/query")
    public ResponseEntity<ApiResponse<List<Review>>> getReviewsByIds(
            @RequestBody List<String> ids) {

        List<Review> reviews = reviewService.getReviewByIds(ids);
        ApiResponse<List<Review>> apiResponse = ApiResponse.success(reviews);
        log.info("Get reviews successfully");
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}

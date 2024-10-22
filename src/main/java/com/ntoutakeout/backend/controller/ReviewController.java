package com.ntoutakeout.backend.controller;

import com.ntoutakeout.backend.entity.Review;
import com.ntoutakeout.backend.service.ReviewService;
import com.ntoutakeout.backend.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
@Slf4j
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping("/getReviewsByIds")
    public ResponseEntity<List<Review>> getReviewsByIds(@RequestBody List<String> ids) {
        log.info("Fetch API: getReviewsByIds Success");
        List<Review> reviews = reviewService.getReviewByIds(ids);
        return ResponseEntity.ok(reviews);
    }
}

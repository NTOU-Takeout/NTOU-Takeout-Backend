package com.ntoutakeout.backend.controller;

import com.ntoutakeout.backend.entity.Review;
import com.ntoutakeout.backend.service.ReviewService;
import com.ntoutakeout.backend.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping("/getReviewsByIds")
    public ResponseEntity<List<Review>> getReviewsByIds(@RequestBody List<String> ids) {
        List<Review> reviews = reviewService.getReviewByIds(ids);
        return ResponseEntity.ok(reviews);
    }
}

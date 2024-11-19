package com.ntoutakeout.backend.controller;

import com.ntoutakeout.backend.entity.Review;
import com.ntoutakeout.backend.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
@Slf4j
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/getReviewsByIds")
    public ResponseEntity<List<Review>> getReviewsByIds(@RequestBody List<String> ids) {
        log.info("Fetch API: getReviewsByIds Success");
        List<Review> reviews = reviewService.getReviewByIds(ids);
        return ResponseEntity.status(HttpStatus.OK).body(reviews);
    }
}

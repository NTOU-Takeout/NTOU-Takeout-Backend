package com.ordernow.backend.review.controller.v1;

import com.ordernow.backend.common.dto.ApiResponse;
import com.ordernow.backend.review.model.entity.Review;
import com.ordernow.backend.review.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("ReviewControllerV1")
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private static final Logger log = LoggerFactory.getLogger(ReviewController.class);
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/query")
    public ResponseEntity<ApiResponse<List<Review>>> getReviewsByIds(
            @RequestBody List<String> ids) {

        List<Review> reviews = reviewService.getReviewByIds(ids);
        ApiResponse<List<Review>> apiResponse = ApiResponse.success(reviews);
        log.info("Get reviews successfully");
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}

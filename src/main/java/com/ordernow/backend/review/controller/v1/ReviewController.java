package com.ordernow.backend.review.controller.v1;

import com.ordernow.backend.auth.model.entity.CustomUserDetail;
import com.ordernow.backend.common.dto.ApiResponse;
import com.ordernow.backend.common.exception.RequestValidationException;
import com.ordernow.backend.common.validation.RequestValidator;
import com.ordernow.backend.review.model.dto.ReviewRequest;
import com.ordernow.backend.review.model.entity.Review;
import com.ordernow.backend.review.service.ReviewService;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController("ReviewControllerV1")
@RequestMapping("/api/v1/reviews")
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/query")
    public ResponseEntity<ApiResponse<List<Review>>> getReviewsByIds(
            @RequestBody List<String> ids)
            throws RequestValidationException {

        RequestValidator.validateRequest(ids);
        List<Review> reviews = reviewService.getReviewByIds(ids);
        ApiResponse<List<Review>> apiResponse = ApiResponse.success(reviews);
        log.info("Get reviews successfully");
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping()
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<Void>> AddReviewToStore(
            @PathParam(value = "storeId") String storeId,
            @RequestBody ReviewRequest reviewRequest,
            @AuthenticationPrincipal CustomUserDetail customUserDetail)
            throws NoSuchElementException, RequestValidationException {

        RequestValidator.validateRequest(reviewRequest);
        reviewService.addNewReviewToStore(
                storeId,
                reviewRequest,
                customUserDetail.getId(),
                customUserDetail.getName());
        ApiResponse<Void> apiResponse = ApiResponse.success(null);
        log.info("Add review successfully");
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}

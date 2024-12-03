package com.ordernow.backend.review.service;

import com.ordernow.backend.review.entity.Review;
import com.ordernow.backend.review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> getReviewByIds(List<String> ids) {
        return reviewRepository.findAllById(ids);
    }
}

package com.ntoutakeout.backend.service;

import com.ntoutakeout.backend.entity.Review;
import com.ntoutakeout.backend.repository.ReviewRepository;
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

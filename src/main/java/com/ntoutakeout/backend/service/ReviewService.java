package com.ntoutakeout.backend.service;

import com.ntoutakeout.backend.entity.Review;
import com.ntoutakeout.backend.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    public List<Review> getReviewByIds(List<String> ids) {
        List<Review> reviews = new ArrayList<>();
        for (String id : ids) {
            reviews.add(reviewRepository.findById(id).get());
        }
        return reviews;
    }
}

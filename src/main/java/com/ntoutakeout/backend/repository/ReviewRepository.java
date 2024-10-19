package com.ntoutakeout.backend.repository;

import com.ntoutakeout.backend.entity.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findAllById(List<String> ids);
}

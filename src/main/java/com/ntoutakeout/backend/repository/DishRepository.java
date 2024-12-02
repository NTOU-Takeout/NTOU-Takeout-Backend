package com.ntoutakeout.backend.repository;

import com.ntoutakeout.backend.entity.Dish;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends MongoRepository<Dish, String> {
    List<Dish> findAllByCategory(String category);
}

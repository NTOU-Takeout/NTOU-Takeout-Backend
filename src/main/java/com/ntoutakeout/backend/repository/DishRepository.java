package com.ntoutakeout.backend.repository;

import com.ntoutakeout.backend.entity.Dish;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DishRepository extends MongoRepository<Dish, String> {
    List<Dish> findAllByCategory(String category);
    Dish findDishById(String id);
}

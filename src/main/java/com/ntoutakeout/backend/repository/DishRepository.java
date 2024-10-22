package com.ntoutakeout.backend.repository;

import com.ntoutakeout.backend.entity.Dish;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DishRepository extends MongoRepository<Dish, String> {
}

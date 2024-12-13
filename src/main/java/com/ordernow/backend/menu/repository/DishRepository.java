package com.ordernow.backend.menu.repository;

import com.ordernow.backend.menu.model.entity.Dish;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends MongoRepository<Dish, String> {
    List<Dish> findAllByCategory(String category);
}

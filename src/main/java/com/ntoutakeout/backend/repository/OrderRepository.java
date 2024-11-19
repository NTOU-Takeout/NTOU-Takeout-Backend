package com.ntoutakeout.backend.repository;

import com.ntoutakeout.backend.entity.Review;
import com.ntoutakeout.backend.entity.order.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
}

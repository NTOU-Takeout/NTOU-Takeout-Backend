package com.ordernow.backend.repository;

import com.ordernow.backend.entity.order.Order;
import com.ordernow.backend.entity.order.OrderedStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    Order findByCustomerIdAndStatus(String customerId, OrderedStatus status);
}

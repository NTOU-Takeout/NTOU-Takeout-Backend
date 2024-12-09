package com.ordernow.backend.order.repository;

import com.ordernow.backend.order.model.entity.Order;
import com.ordernow.backend.order.model.entity.OrderedStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    Order findByCustomerIdAndStatus(String customerId, OrderedStatus status);
    List<Order> findAllByCustomerIdAndStatus(String customerId, OrderedStatus status);
}

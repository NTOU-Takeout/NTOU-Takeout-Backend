package com.ordernow.backend.order.repository;

import com.ordernow.backend.order.model.entity.Order;
import com.ordernow.backend.order.model.entity.OrderedStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    Order findByCustomerIdAndStatus(String customerId, OrderedStatus status);

    List<Order> findAllByCustomerIdAndStatus(String customerId, OrderedStatus status, Pageable pageable);
    List<Order> findAllByCustomerIdAndStatusNot(String id, OrderedStatus status, Pageable pageable);

    List<Order> findAllByStoreIdAndStatus(String storeId, OrderedStatus status, Pageable pageable);
    List<Order> findAllByStoreIdAndStatusNot(String id, OrderedStatus status, Pageable pageable);

    int countByCustomerIdAndStatus(String customerId, OrderedStatus status);
    int countByCustomerIdAndStatusNot(String customerId, OrderedStatus status);

    int countByStoreIdAndStatus(String storeId, OrderedStatus status);
    int countByStoreIdAndStatusNot(String storeId, OrderedStatus status);
}

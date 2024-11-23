package com.ntoutakeout.backend.service;

import com.ntoutakeout.backend.dto.order.OrderedDishPatchRequest;
import com.ntoutakeout.backend.entity.order.Order;
import com.ntoutakeout.backend.entity.order.OrderedDish;
import com.ntoutakeout.backend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order getCart(String customerId) {
        return null;
    }

    public Order createCart(String customerId) {
        return null;
    }

    public void deleteCart(String customerId) {

    }

    public Order addNewDish(String customerId, OrderedDish dish) {
        return null;
    }

    public Order updateDish(String customerId, String dishId, OrderedDishPatchRequest request) {
        return null;
    }

    public Order sendOrder(String customerId) {
        return null;
    }

    public void cancelOrder(String customerId) {
    }
}

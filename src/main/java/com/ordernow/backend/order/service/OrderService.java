package com.ordernow.backend.order.service;

import com.ordernow.backend.order.model.entity.Order;
import com.ordernow.backend.order.model.entity.OrderedStatus;
import com.ordernow.backend.order.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order getOrderAndValid(String orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            throw new NoSuchElementException("Order not found with ID: " + orderId);
        }

        return order;
    }

    public void cancelOrder(String orderId)
            throws NoSuchElementException, IllegalStateException {

        Order order = getOrderAndValid(orderId);
        if(order.getStatus() != OrderedStatus.PENDING) {
            throw new IllegalStateException("Order is not in PENDING status");
        }
        order.setStatus(OrderedStatus.CANCELED);
        orderRepository.save(order);
    }

    public void updateStatus(String orderId, OrderedStatus status)
            throws NoSuchElementException, IllegalStateException {

        Order order = getOrderAndValid(orderId);

        if(status == OrderedStatus.PROCESSING && order.getStatus() != OrderedStatus.PENDING) {
            throw new IllegalStateException("Order is not in PENDING status");
        }
        if(status == OrderedStatus.COMPLETED && order.getStatus() != OrderedStatus.PROCESSING) {
            throw new IllegalStateException("Order is not in PROCESSING status");
        }
        if(status == OrderedStatus.PICKED_UP && order.getStatus() != OrderedStatus.COMPLETED) {
            throw new IllegalStateException("Order is not in COMPLETED status");
        }

        order.setStatus(status);
        orderRepository.save(order);
    }
}

package com.ntoutakeout.backend.service;

import com.ntoutakeout.backend.dto.order.OrderedDishPatchRequest;
import com.ntoutakeout.backend.entity.order.Order;
import com.ntoutakeout.backend.entity.order.OrderedDish;
import com.ntoutakeout.backend.entity.order.OrderedStatus;
import com.ntoutakeout.backend.entity.order.ChosenAttribute;
import com.ntoutakeout.backend.repository.DishRepository;
import com.ntoutakeout.backend.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
@Slf4j

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final DishRepository dishRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, DishRepository dishRepository) {
        this.orderRepository = orderRepository;
        this.dishRepository = dishRepository;
    }

    public Order getOrderById(String orderId) {
        log.info("getOrderById: {}", orderId);
        Order cart = orderRepository.findOrderById(orderId);
        log.info("Order found: {}", cart);
        return cart;
    }

    public Order getCart(String customerId) {
        log.info("Getting cart for customer: {}", customerId);
        Order cart = orderRepository.findByCustomerIdAndStatus(customerId, OrderedStatus.IN_CART);
        log.info("IN_Cart Cart found: {}", cart);
        return cart;
    }

    public Order getPendingCart(String customerId) {
        log.info("Getting PendingCart for customer: {}", customerId);
        Order cart = orderRepository.findByCustomerIdAndStatus(customerId, OrderedStatus.PENDING);
        log.info("Pending Cart found: {}", cart);
        return cart;
    }

    public Order createCart(String customerId) {
        Order cart = new Order();
        cart.setCustomerId(customerId);
        cart.setStatus(OrderedStatus.IN_CART);
        cart.setCost(0.0);
        cart.setDate(LocalDateTime.now());
        return orderRepository.save(cart);
    }

    public void deleteCart(String customerId) {
        Order cart = getCart(customerId);
        orderRepository.delete(cart);
    }

    public Order addNewDish(String customerId, OrderedDish dish) {
        Order cart = getCart(customerId);
        cart.getOrderedDishes().add(dish);
        dish.setPrice(dishRepository.findDishById(dish.getDishId()).getPrice());
        dish.setDishName(dishRepository.findDishById(dish.getDishId()).getName());
        updateOrderCost(cart);
        return orderRepository.save(cart);
    }

    public Order updateDish(String customerId, String dishId, OrderedDishPatchRequest request) {
        Order cart = getCart(customerId);
        cart.getOrderedDishes().stream()
            .filter(dish -> dish.getDishId().equals(dishId))
            .findFirst()
            .ifPresent(dish -> {
                if (request.getQuantity() != null) {
                    dish.setQuantity(request.getQuantity());
                }
                if (request.getNote() != null) {
                    dish.setNote(request.getNote());
                }
                if (request.getChosenAttributes() != null) {
                    dish.setChosenAttributes(request.getChosenAttributes());
                }
            });
        updateOrderCost(cart);
        return orderRepository.save(cart);
    }

    public Order sendOrder(String customerId) {
        Order cart = getCart(customerId);
        cart.setStatus(OrderedStatus.PENDING);
        cart.setDate(LocalDateTime.now());
        return orderRepository.save(cart);
    }

    public void cancelOrder(String orderId) {
        Order cart = getOrderById(orderId);
        if(cart.getStatus() != OrderedStatus.PENDING) {
            log.info("order is not Pending, you can not cancel: {}", cart);
            return;
        }
        cart.setStatus(OrderedStatus.CANCELED);
        orderRepository.save(cart);
    }

    private void updateOrderCost(Order order) {
        double totalCost = order.getOrderedDishes().stream()
            .mapToDouble(dish -> {
                double dishTotal = dish.getPrice() * dish.getQuantity();
                double attributesTotal = dish.getChosenAttributes().stream()
                    .mapToDouble(ChosenAttribute::getExtraCost)
                    .sum();
                return dishTotal + (attributesTotal * dish.getQuantity());
            })
            .sum();
        order.setCost(totalCost);
    }
}

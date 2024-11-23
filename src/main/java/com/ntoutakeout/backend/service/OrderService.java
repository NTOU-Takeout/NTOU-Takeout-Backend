package com.ntoutakeout.backend.service;

import com.ntoutakeout.backend.dto.order.OrderedDishPatchRequest;
import com.ntoutakeout.backend.entity.order.Order;
import com.ntoutakeout.backend.entity.order.OrderedDish;
import com.ntoutakeout.backend.entity.order.OrderedStatus;
import com.ntoutakeout.backend.entity.order.ChosenAttribute;
import com.ntoutakeout.backend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order getCart(String customerId) {
        return orderRepository.findByIdAndStatus(customerId, OrderedStatus.IN_CART);
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

    public void cancelOrder(String customerId) {
        Order cart = getCart(customerId);
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

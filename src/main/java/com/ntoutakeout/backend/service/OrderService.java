package com.ntoutakeout.backend.service;

import com.ntoutakeout.backend.entity.order.ChosenAttribute;
import com.ntoutakeout.backend.entity.order.Order;
import com.ntoutakeout.backend.entity.order.OrderedDish;
import com.ntoutakeout.backend.entity.order.OrderedStatus;
import com.ntoutakeout.backend.repository.OrderRepository;
import com.ntoutakeout.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class OrderService {

    private OrderRepository orderRepository;

    public String createOrder(Map<String, Object> cartRequest, String customerId) {

        Order order = new Order();
        order.setCustomerId(customerId);

        order.setStoreId((String) cartRequest.get("storeId"));
        order.setDate(LocalDateTime.now());
        order.setStatus(OrderedStatus.IN_CART);

        List<OrderedDish> orderedDishes = (List<OrderedDish>) cartRequest.get("orderedDishes");
        order.setOrderedDishes(orderedDishes);

        order.calculateTotalCost();

        Order savedOrder = orderRepository.save(order);

        return savedOrder.getId();
    }


    public String updateOrder(Map<String, Object> updateRequest, String orderId) {

        Order order = orderRepository.findById(orderId).orElse(null);

        if (order == null) {
            return null;
        }

        String dishId = (String) updateRequest.get("dishId");
        Integer quantity = (Integer) updateRequest.get("quantity");
        String note = (String) updateRequest.get("note");
        List<ChosenAttribute> chosenAttributes = (List<ChosenAttribute>) updateRequest.get("chosenAttributes");

        OrderedDish existingDish = order.getOrderedDishes().stream()
                .filter(dish -> Objects.equals(dish.getDishId(), dishId))
                .findFirst()
                .orElse(null);

        if (existingDish != null) {
            existingDish.setQuantity(quantity);
            existingDish.setNote(note);
            existingDish.setChosenAttributes(chosenAttributes);
        } else {
            OrderedDish newDish = new OrderedDish();
            newDish.setDishId(dishId);
            newDish.setQuantity(quantity);
            newDish.setNote(note);
            newDish.setChosenAttributes(chosenAttributes);
            order.getOrderedDishes().add(newDish);
        }

        order.calculateTotalCost();

        Order updatedOrder = orderRepository.save(order);

        return updatedOrder.getId();
    }

}

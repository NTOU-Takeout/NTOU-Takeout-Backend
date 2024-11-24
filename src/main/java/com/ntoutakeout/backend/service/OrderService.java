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
        try {
            log.info("Attempting to retrieve order with ID: {}", orderId);
            Order order = orderRepository.findOrderById(orderId);
            if (order == null) {
                log.error("Order not found with ID: {}", orderId);
                throw new RuntimeException("Order not found");
            }
            log.info("Successfully retrieved order: {}", order);
            return order;
        } catch (Exception e) {
            log.error("Error occurred while getting order by ID: {}", e.getMessage());
            throw new RuntimeException("Failed to get order: " + e.getMessage());
        }
    }

    public Order getCart(String customerId) {
        try {
            log.info("Attempting to retrieve cart for customer ID: {}", customerId);
            Order cart = orderRepository.findByCustomerIdAndStatus(customerId, OrderedStatus.IN_CART);
            if (cart == null) {
                log.info("No existing cart found for customer ID: {}. Creating new cart.", customerId);
                return createCart(customerId);
            }
            log.info("Successfully retrieved active cart: {}", cart);
            return cart;
        } catch (Exception e) {
            log.error("Error occurred while getting cart: {}", e.getMessage());
            throw new RuntimeException("Failed to get cart: " + e.getMessage());
        }
    }

    public Order getPendingCart(String customerId) {
        try {
            log.info("Attempting to retrieve pending cart for customer ID: {}", customerId);
            Order cart = orderRepository.findByCustomerIdAndStatus(customerId, OrderedStatus.PENDING);
            if (cart == null) {
                log.error("No pending cart found for customer ID: {}", customerId);
                throw new RuntimeException("No pending cart found");
            }
            log.info("Successfully retrieved pending cart: {}", cart);
            return cart;
        } catch (Exception e) {
            log.error("Error occurred while getting pending cart: {}", e.getMessage());
            throw new RuntimeException("Failed to get pending cart: " + e.getMessage());
        }
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
        try {
            Order cart = getCart(customerId);
            if (cart == null) {
                log.error("Cart not found for customer: {}", customerId);
                throw new RuntimeException("Cart not found");
            }

            var dishEntity = dishRepository.findDishById(dish.getDishId());
            if (dishEntity == null) {
                log.error("Dish not found with ID: {}", dish.getDishId());
                throw new RuntimeException("Dish not found");
            }

            cart.getOrderedDishes().add(dish);
            dish.setPrice(dishEntity.getPrice());
            dish.setDishName(dishEntity.getName());
            updateOrderCost(cart);
            return orderRepository.save(cart);
        } catch (Exception e) {
            log.error("Error occurred while adding new dish: {}", e.getMessage());
            throw new RuntimeException("Failed to add dish: " + e.getMessage());
        }
    }

    public Order updateDish(String customerId, String dishId, OrderedDishPatchRequest request) {
        try {
            if (request == null) {
                throw new RuntimeException("Update request cannot be null");
            }

            Order cart = getCart(customerId);
            if (cart == null) {
                log.error("Cart not found for customer: {}", customerId);
                throw new RuntimeException("Cart not found");
            }

            if (request.getQuantity() != null && request.getQuantity() == 0) {
                log.info("Removing dish {} from cart as quantity is 0", dishId);
                cart.getOrderedDishes().removeIf(dish -> dish.getDishId().equals(dishId));
            } else {
                OrderedDish dishToUpdate = cart.getOrderedDishes().stream()
                    .filter(dish -> dish.getDishId().equals(dishId))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Dish not found in cart"));

                if (request.getQuantity() != null) {
                    dishToUpdate.setQuantity(request.getQuantity());
                }
                if (request.getNote() != null) {
                    dishToUpdate.setNote(request.getNote());
                }
                if (request.getChosenAttributes() != null) {
                    dishToUpdate.setChosenAttributes(request.getChosenAttributes());
                }
            }

            updateOrderCost(cart);
            return orderRepository.save(cart);
        } catch (Exception e) {
            log.error("Error occurred while updating dish: {}", e.getMessage());
            throw new RuntimeException("Failed to update dish: " + e.getMessage());
        }
    }

    public Order sendOrder(String customerId) {
        try {
            Order cart = getCart(customerId);
            if (cart == null) {
                throw new RuntimeException("Cart not found");
            }
            cart.setStatus(OrderedStatus.PENDING);
            cart.setDate(LocalDateTime.now());
            return orderRepository.save(cart);
        } catch (Exception e) {
            log.error("Error occurred while sending order: {}", e.getMessage());
            throw new RuntimeException("Failed to send order: " + e.getMessage());
        }
    }

    public void cancelOrder(String orderId) {
        try {
            Order cart = getOrderById(orderId);
            if (cart == null) {
                throw new RuntimeException("Order not found");
            }
            if(cart.getStatus() != OrderedStatus.PENDING) {
                log.info("Cannot cancel order ID: {}. Order status is not PENDING. Current status: {}", 
                    orderId, cart.getStatus());
                throw new RuntimeException("Can only cancel pending orders");
            }
            cart.setStatus(OrderedStatus.CANCELED);
            orderRepository.save(cart);
        } catch (Exception e) {
            log.error("Error occurred while canceling order: {}", e.getMessage());
            throw new RuntimeException("Failed to cancel order: " + e.getMessage());
        }
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

package com.ntoutakeout.backend.service;

import com.ntoutakeout.backend.dto.order.OrderedDishPatchRequest;
import com.ntoutakeout.backend.entity.Dish;
import com.ntoutakeout.backend.entity.order.Order;
import com.ntoutakeout.backend.entity.order.OrderedDish;
import com.ntoutakeout.backend.entity.order.OrderedStatus;
import com.ntoutakeout.backend.entity.order.ChosenAttribute;
import com.ntoutakeout.backend.entity.user.Customer;
import com.ntoutakeout.backend.repository.DishRepository;
import com.ntoutakeout.backend.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Slf4j

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final DishRepository dishRepository;
    private final CustomerService  customerService;

    @Autowired
    public OrderService(OrderRepository orderRepository, DishRepository dishRepository, CustomerService customerService) {
        this.orderRepository = orderRepository;
        this.dishRepository = dishRepository;
        this.customerService = customerService;
    }

    public Order getOrderAndValid(String orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            throw new NoSuchElementException("Order not found with ID: " + orderId);
        }
        return order;
    }

    public Order findCart(String customerId) {
        return orderRepository.findByCustomerIdAndStatus(customerId, OrderedStatus.IN_CART);
    }

    public Order createCart(String customerId) {
        log.info("No existing cart found for customer ID: {}. Creating new cart.", customerId);
        Order cart = new Order(customerId);
        return orderRepository.save(cart);
    }

    public void validDishId(String dishId) {
        Dish dish = dishRepository.findById(dishId).orElse(null);
        if (dish == null) {
            throw new NoSuchElementException("Dish not found with ID: " + dishId);
        }
    }

    public Order getCart(String customerId)
            throws NoSuchElementException {
        Customer customer = customerService.getCustomerById(customerId);
        return findCart(customerId) == null
                ? createCart(customerId)
                : findCart(customerId);
    }

    public void deleteCart(String customerId)
            throws NoSuchElementException {
        Customer customer = customerService.getCustomerById(customerId);
        Order cart = getCart(customerId);
        orderRepository.delete(cart);
    }

    public Order addNewDish(String customerId, OrderedDish orderedDish)
            throws NoSuchElementException {
        Customer customer = customerService.getCustomerById(customerId);
        Order cart = findCart(customerId);
        if (cart == null) {
            throw new NoSuchElementException("Cart not found with Customer ID: " + customerId);
        }

        validDishId(orderedDish.getDishId());

        if(cart.getStoreId() == null) {
            cart.setStoreId(orderedDish.getStoreId());
        }
        else if(!cart.getStoreId().equals(orderedDish.getStoreId())) {
            throw new NoSuchElementException("Cart has different stores");
        }

        for(OrderedDish dish : cart.getOrderedDishes()) {
            if(dish.equalsWithoutId(orderedDish)) {
                dish.setQuantity(dish.getQuantity() + orderedDish.getQuantity());
                updateOrderCost(cart);
                return orderRepository.save(cart);
            }
        }

        cart.getOrderedDishes().add(orderedDish);
        updateOrderCost(cart);
        return orderRepository.save(cart);
    }

    public Order updateDish(String customerId, String orderedDishId, OrderedDishPatchRequest request)
            throws NoSuchElementException, IllegalArgumentException {

        Customer customer = customerService.getCustomerById(customerId);
        if (request == null) {
            throw new IllegalArgumentException("Update request cannot be null");
        }

        Order cart = findCart(customerId);
        if (cart == null) {
            throw new NoSuchElementException("Cart not found");
        }

        if (request.getQuantity() != null && request.getQuantity() == 0) {
            log.info("Removing dish {} from cart as quantity is 0", orderedDishId);
            cart.getOrderedDishes().removeIf(dish -> dish.getId().equals(orderedDishId));
        } else {
            OrderedDish dishToUpdate = cart.getOrderedDishes().stream()
                    .filter(dish -> dish.getId().equals(orderedDishId))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Dish not found in cart"));

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
    }

    public Order sendOrder(String customerId)
            throws NoSuchElementException {

        Order cart = findCart(customerId);
        if (cart == null) {
            throw new NoSuchElementException("Cart not found");
        }

        cart.setStatus(OrderedStatus.PENDING);
        cart.setDate(LocalDateTime.now());
        return orderRepository.save(cart);
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

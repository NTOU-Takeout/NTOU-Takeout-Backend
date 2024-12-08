package com.ordernow.backend.order.service;

import com.ordernow.backend.order.model.dto.OrderedDishPatchRequest;
import com.ordernow.backend.menu.model.entity.Dish;
import com.ordernow.backend.order.model.entity.Order;
import com.ordernow.backend.order.model.entity.OrderedDish;
import com.ordernow.backend.order.model.entity.OrderedStatus;
import com.ordernow.backend.order.model.entity.ChosenAttribute;
import com.ordernow.backend.menu.repository.DishRepository;
import com.ordernow.backend.order.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class CartService {

    private final OrderRepository orderRepository;
    private final DishRepository dishRepository;

    @Autowired
    public CartService(OrderRepository orderRepository, DishRepository dishRepository) {
        this.orderRepository = orderRepository;
        this.dishRepository = dishRepository;
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

    public Order getCart(String customerId) {
        return findCart(customerId) == null
                ? createCart(customerId)
                : findCart(customerId);
    }

    public void deleteCart(String customerId)
            throws NoSuchElementException {

        Order cart = findCart(customerId);
        if(cart == null) {
            throw new NoSuchElementException("Cart not found with ID: " + customerId);
        }
        orderRepository.delete(cart);
    }

    public Order addNewDish(String customerId, OrderedDish orderedDish)
            throws NoSuchElementException, IllegalArgumentException {

        Order cart = findCart(customerId);
        if (cart == null) {
            throw new NoSuchElementException("Cart not found with Customer ID: " + customerId);
        }

        validDishId(orderedDish.getDishId());

        if(cart.getStoreId() == null) {
            cart.setStoreId(orderedDish.getStoreId());
        }
        else if(!cart.getStoreId().equals(orderedDish.getStoreId())) {
            throw new IllegalArgumentException("Cart has different stores");
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

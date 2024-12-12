package com.ordernow.backend.order.service;

import com.ordernow.backend.menu.model.entity.AttributeOption;
import com.ordernow.backend.menu.model.entity.DishAttribute;
import com.ordernow.backend.order.model.dto.OrderedDishPatchRequest;
import com.ordernow.backend.menu.model.entity.Dish;
import com.ordernow.backend.order.model.dto.OrderedDishRequest;
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
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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

    public void validOrderedDish(OrderedDish orderedDish) {
        Dish dish = dishRepository.findById(orderedDish.getDishId()).orElse(null);
        if (dish == null) {
            throw new NoSuchElementException("Dish not found with ID: " + orderedDish.getDishId());
        }

        Map<String, Map<String, Double>> validAttributeOptions = dish.getDishAttributes().stream()
                .collect(Collectors.toMap(
                        DishAttribute::getName,
                        attr -> attr.getAttributeOptions().stream()
                                .collect(Collectors.toMap(
                                        AttributeOption::getName,
                                        AttributeOption::getExtraCost
                                ))
                ));

        for(ChosenAttribute attribute : orderedDish.getChosenAttributes()) {
            Map<String, Double> options = validAttributeOptions.get(attribute.getAttributeName());
            if(options == null) {
                throw new NoSuchElementException("Attribute not found with name: " + attribute.getAttributeName());
            }

            Double extraCost = options.get(attribute.getChosenOption());
            if(extraCost == null) {
                throw new NoSuchElementException("Option not found with name: " + attribute.getChosenOption());
            }

            if(!attribute.getExtraCost().equals(extraCost)) {
                throw new NoSuchElementException("ExtraCost not found with option: " + attribute.getChosenOption());
            }
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

    public String addNewDish(String customerId, OrderedDishRequest orderedDishRequest)
            throws NoSuchElementException, IllegalArgumentException {

        Order cart = findCart(customerId);
        if (cart == null) {
            throw new NoSuchElementException("Cart not found with Customer ID: " + customerId);
        }

        validDishId(orderedDishRequest.getDishId());

        if(cart.getStoreId() == null) {
            cart.setStoreId(orderedDishRequest.getStoreId());
        }
        else if(!cart.getStoreId().equals(orderedDishRequest.getStoreId())) {
            throw new IllegalArgumentException("Cart has different stores");
        }

        for(OrderedDish orderedDish : cart.getOrderedDishes()) {
            if(orderedDish.equals(orderedDishRequest)) {
                orderedDish.setQuantity(orderedDish.getQuantity() + orderedDishRequest.getQuantity());
                updateOrderCost(cart);
                orderRepository.save(cart);
                return orderedDish.getId();
            }
        }

        Dish dish = dishRepository.findById(orderedDishRequest.getDishId()).orElse(null);
        if(dish == null){
            throw new NoSuchElementException("Dish not found with ID: " + orderedDishRequest.getDishId());
        }

        OrderedDish orderedDish = new OrderedDish(orderedDishRequest, dish);
        validOrderedDish(orderedDish);
        cart.getOrderedDishes().add(orderedDish);
        updateOrderCost(cart);
        orderRepository.save(cart);
        return orderedDish.getId();
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
            validOrderedDish(dishToUpdate);
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

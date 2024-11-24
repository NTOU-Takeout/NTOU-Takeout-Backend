package com.ntoutakeout.backend.service;

import com.ntoutakeout.backend.entity.Dish;
import com.ntoutakeout.backend.entity.order.ChosenAttribute;
import com.ntoutakeout.backend.entity.order.Order;
import com.ntoutakeout.backend.entity.order.OrderedDish;
import com.ntoutakeout.backend.entity.order.OrderedStatus;
import com.ntoutakeout.backend.entity.user.Customer;
import com.ntoutakeout.backend.entity.user.Role;
import com.ntoutakeout.backend.entity.user.User;
import com.ntoutakeout.backend.repository.OrderRepository;
import com.ntoutakeout.backend.repository.UserRepository;
import org.bson.codecs.jsr310.LocalDateCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public CustomerService(UserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    public Customer getCustomer(String customerId) throws Exception {
        Customer customer = (Customer) userRepository.findById(customerId).orElse(null);
        if (customer == null || customer.getRole() != Role.CUSTOMER) {
            throw new Exception("Customer not found");
        }
        return customer;
    }

    public Order getCart(String customerId) throws Exception {
        Customer customer = getCustomer(customerId);

        Order cartOrder = orderRepository.findByCustomerIdAndStatus(customerId, OrderedStatus.IN_CART);
        if (cartOrder == null) {
            cartOrder = createCart(customerId);
            customer.getOrderList().add(cartOrder);
        }
        return cartOrder;
    }

    public Order createCart(String customerId) {
        Order order = new Order();
        order.setCustomerId(customerId);
        order.setStatus(OrderedStatus.IN_CART);
        order.setCost(0.0);
        orderRepository.save(order);
        return order;
    }

    public void deleteCart(String customerId, Order cartOrder) throws Exception {
        Customer customer = getCustomer(customerId);
        customer.getOrderList().remove(cartOrder);
    }

    public Order addNewDish(String customerId, OrderedDish dish) throws Exception {
        Customer customer = getCustomer(customerId);

        Order cartOrder = getCart(customerId);
        cartOrder.getOrderedDishes().add(dish);
        return cartOrder;
    }

    public Order updateDish(String customerId, String dishId, Map<String, Object> updates) throws Exception {
        Customer customer = getCustomer(customerId);

        Order cartOrder = getCart(customerId);
        OrderedDish orderedDish = cartOrder.getOrderedDishes()
                .stream()
                .filter(dish -> dish.getDishId().equals(dishId))
                .findFirst()
                .orElse(null);

        if(orderedDish == null) {
            throw new Exception("Dish not found");
        }

        updates.forEach((key, value) -> {
            switch (key) {
                case "quantity":
                    orderedDish.setQuantity((Integer) value);
                    break;
                case "note":
                    orderedDish.setNote((String) value);
                    break;
            }
        });

        return cartOrder;
    }

    public Order sendOrder(String customerId) throws Exception{
        Customer customer = getCustomer(customerId);

        Order cartOrder = orderRepository.findByCustomerIdAndStatus(customerId, OrderedStatus.IN_CART);

        if (cartOrder == null) {
            throw new Exception("Order not found");
        }

        cartOrder.setStatus(OrderedStatus.PENDING);
        return cartOrder;
    }

    public void cancelOrder(String customerId) throws Exception{
        Customer customer = getCustomer(customerId);

        Order cartOrder = orderRepository.findByCustomerIdAndStatus(customerId, OrderedStatus.IN_CART);

        if (cartOrder == null) {
            throw new Exception("Order not found");
        }

        cartOrder.setStatus(OrderedStatus.CANCELED);
    }
}

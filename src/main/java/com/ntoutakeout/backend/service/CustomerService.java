package com.ntoutakeout.backend.service;

import com.ntoutakeout.backend.entity.Cart;
import com.ntoutakeout.backend.entity.order.Order;
import com.ntoutakeout.backend.entity.order.OrderedStatus;
import com.ntoutakeout.backend.entity.user.Customer;
import com.ntoutakeout.backend.entity.user.Role;
import com.ntoutakeout.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final UserRepository userRepository;

    @Autowired
    public CustomerService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Order getCart(String customerId) throws Exception {
        Customer customer = (Customer) userRepository.findById(customerId).orElse(null);
        if (customer == null || customer.getRole() != Role.CUSTOMER) {
            throw new Exception("Customer not found");
        }
        Order cartOrder = customer
                .getOrderList()
                .stream()
                .filter(cart -> OrderedStatus.IN_CART.equals(cart.getStatus()))
                .findFirst()
                .orElse(null);
    }
}

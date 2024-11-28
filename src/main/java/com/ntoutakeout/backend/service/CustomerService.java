package com.ntoutakeout.backend.service;

import com.ntoutakeout.backend.entity.user.Customer;
import com.ntoutakeout.backend.entity.user.Role;
import com.ntoutakeout.backend.repository.OrderRepository;
import com.ntoutakeout.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);
    private final UserRepository userRepository;

    @Autowired
    public CustomerService(UserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
    }

    public Customer getCustomerById(String customerId) {
        Customer customer = (Customer) userRepository.findById(customerId).orElse(null);
        if (customer == null) {
            throw new NoSuchElementException("Customer not found with ID: " + customerId);
        }
        if(customer.getRole() != Role.CUSTOMER) {
            throw new NoSuchElementException("User is not a customer");
        }
        return customer;
    }
}

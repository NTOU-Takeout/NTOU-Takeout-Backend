package com.ntoutakeout.backend.controller.v1;

import com.ntoutakeout.backend.entity.Cart;
import com.ntoutakeout.backend.entity.user.Customer;
import com.ntoutakeout.backend.service.CustomerService;
import com.ntoutakeout.backend.service.JWTService;
import com.ntoutakeout.backend.service.OrderService;
import com.ntoutakeout.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/customer")
@Slf4j
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{customerId}/cart")
    public ResponseEntity<?> getCart(@PathVariable("customerId") String customerId) {
        try {
            Cart cart = customerService.getCart(customerId);
            log.info("Customer get cart successfully");
            return ResponseEntity.status(HttpStatus.OK).body(cart);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

package com.ntoutakeout.backend.controller.v1;

import com.ntoutakeout.backend.dto.order.OrderedDishPatchRequest;
import com.ntoutakeout.backend.entity.order.Order;
import com.ntoutakeout.backend.entity.order.OrderedDish;
import com.ntoutakeout.backend.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/customer")
@Slf4j
public class CustomerController {

    public final OrderService orderService;

    @Autowired
    public CustomerController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{customerId}/cart")
    public ResponseEntity<?> getCart(
            @PathVariable("customerId") String customerId) {
        try {
            Order cartOrder = orderService.getCart(customerId);
            log.info("Customer get cart successfully");
            return ResponseEntity.status(HttpStatus.OK).body(cartOrder);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{customerId}/cart")
    public ResponseEntity<String> deleteCart(
            @PathVariable("customerId") String customerId) {
        try {
            orderService.deleteCart(customerId);
            log.info("Customer delete cart successfully");
            return ResponseEntity.status(HttpStatus.OK).body("success");
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/{customerId}/cart/dishes")
    public ResponseEntity<?> addNewDish(
            @PathVariable("customerId") String customerId,
            @RequestBody OrderedDish dish) throws Exception {

        try {
            Order cartOrder = orderService.addNewDish(customerId, dish);
            return ResponseEntity.status(HttpStatus.OK).body(cartOrder);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PatchMapping("/{customerId}/cart/dishes/{dishId}")
    public ResponseEntity<?> updateDish(
            @PathVariable("customerId") String customerId,
            @PathVariable("dishId") String dishId,
            @RequestBody OrderedDishPatchRequest request) throws Exception {
        log.info("updateDish started");
        try {
            Order cartOrder = orderService.updateDish(customerId, dishId, request);
            log.info("Customer update dish successfully");
            return ResponseEntity.status(HttpStatus.OK).body(cartOrder);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PatchMapping("/{customerId}/cart/send")
    public ResponseEntity<?> sendOrder(
            @PathVariable("customerId") String customerId) throws Exception {
        try {
            Order cartOrder = orderService.sendOrder(customerId);
            return ResponseEntity.status(HttpStatus.OK).body(cartOrder);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PatchMapping("/cart/{orderId}/cancel")
    public ResponseEntity<String> cancelDish(
            @PathVariable("orderId") String orderId) throws Exception {
        try {
            orderService.cancelOrder(orderId);
            return ResponseEntity.status(HttpStatus.OK).body("Success");
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

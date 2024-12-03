package com.ordernow.backend.order.controller.v1;

import com.ordernow.backend.common.dto.ApiResponse;
import com.ordernow.backend.order.model.dto.OrderedDishPatchRequest;
import com.ordernow.backend.order.model.entity.Order;
import com.ordernow.backend.order.model.entity.OrderedDish;
import com.ordernow.backend.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController("CustomerControllerV1")
@RequestMapping("api/v1/customer")
@Slf4j
public class CustomerController {

    public final OrderService orderService;

    @Autowired
    public CustomerController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{customerId}/cart")
    public ResponseEntity<ApiResponse<Order>> getCart(
            @PathVariable("customerId") String customerId)
            throws NoSuchElementException {

        Order cartOrder = orderService.getCart(customerId);
        ApiResponse<Order> apiResponse = ApiResponse.success(cartOrder);
        log.info("Customer get cart successfully");
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @DeleteMapping("/{customerId}/cart")
    public ResponseEntity<ApiResponse<Void>> deleteCart(
            @PathVariable("customerId") String customerId)
            throws NoSuchElementException {

        orderService.deleteCart(customerId);
        ApiResponse<Void> apiResponse = ApiResponse.success(null);
        log.info("Customer delete cart successfully");
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping("/{customerId}/cart/dishes")
    public ResponseEntity<ApiResponse<String>> addNewDish(
            @PathVariable("customerId") String customerId,
            @RequestBody OrderedDish dish)
            throws NoSuchElementException, IllegalArgumentException {

        Order cartOrder = orderService.addNewDish(customerId, dish);
        ApiResponse<String> apiResponse = ApiResponse.success(dish.getId());
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PatchMapping("/{customerId}/cart/dishes/{orderedDishId}")
    public ResponseEntity<ApiResponse<String>> updateDish(
            @PathVariable("customerId") String customerId,
            @PathVariable("orderedDishId") String orderedDishId,
            @RequestBody OrderedDishPatchRequest request)
            throws NoSuchElementException, IllegalArgumentException {

        Order cartOrder = orderService.updateDish(customerId, orderedDishId, request);
        ApiResponse<String> apiResponse = ApiResponse.success(orderedDishId);
        log.info("Customer update dish successfully");
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PatchMapping("/{customerId}/cart/send")
    public ResponseEntity<ApiResponse<Order>> sendOrder(
            @PathVariable("customerId") String customerId)
            throws NoSuchElementException {

        Order cartOrder = orderService.sendOrder(customerId);
        ApiResponse<Order> apiResponse = ApiResponse.success(cartOrder);
        log.info("Customer send order successfully");
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PatchMapping("/cart/{orderId}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelDish(
            @PathVariable("orderId") String orderId)
            throws NoSuchElementException {

        orderService.cancelOrder(orderId);
        ApiResponse<Void> apiResponse = ApiResponse.success(null);
        log.info("Customer cancel dish successfully");
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}

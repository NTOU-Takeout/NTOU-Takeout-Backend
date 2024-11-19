package com.ntoutakeout.backend.controller;

import com.ntoutakeout.backend.entity.order.Order;
import com.ntoutakeout.backend.entity.order.OrderedDish;
import com.ntoutakeout.backend.repository.OrderRepository;
import com.ntoutakeout.backend.service.JWTService;
import com.ntoutakeout.backend.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/order")
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final JWTService jwtService;

    @Autowired
    private OrderController(OrderService orderService, JWTService jwtService) {
        this.orderService = orderService;
        this.jwtService = jwtService;
    }

//    @PostMapping("/create")
//    public ResponseEntity<String> createOrder(@RequestHeader("Authorization") String token,
//                                                   @RequestBody Map<String, Object> cartRequest) {
//
//        log.info("POST API: /cart/update/dishes - Create cart dishes request received");
//
//        if (!jwtService.validateToken(token)) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//        String customerId = jwtService.extractCustomerId(token);
//
//
//        String createdOrderId = orderService.createOrder(cartRequest, customerId);
//        return ResponseEntity.status(HttpStatus.OK).body(createdOrderId);
//    }
//
//    @PutMapping("/update/{OrderId}")
//    public ResponseEntity<String> updateOrder(
//            @PathVariable String OrderId,
//            @RequestHeader("Authorization") String token,
//            @RequestBody Map<String, Object> updateRequest) {
//
//        if (!jwtService.validateToken(token)) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//
//        String orderId = orderService.updateOrder(updateRequest, OrderId);
//
//        if (orderId == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//
//        return ResponseEntity.ok(orderId);
//    }


}

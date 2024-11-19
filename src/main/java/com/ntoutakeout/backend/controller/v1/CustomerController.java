package com.ntoutakeout.backend.controller.v1;

import com.ntoutakeout.backend.service.JWTService;
import com.ntoutakeout.backend.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/customer")
@Slf4j
public class CustomerController {

    private final OrderService orderService;
    private final JWTService jwtService;

    @Autowired
    private CustomerController(OrderService orderService, JWTService jwtService) {
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

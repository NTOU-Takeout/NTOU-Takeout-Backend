package com.ordernow.backend.order.controller.v1;

import com.ordernow.backend.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("CartControllerV1")
@RequestMapping("/api/v1/cart")
@Slf4j
public class CartController {

    public final OrderService orderService;

    @Autowired
    public CartController(OrderService orderService) {
        this.orderService = orderService;
    }

}

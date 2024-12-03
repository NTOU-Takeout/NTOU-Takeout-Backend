package com.ordernow.backend.order.controller.v1;

import com.ordernow.backend.common.dto.ApiResponse;
import com.ordernow.backend.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController("CustomerControllerV1")
@RequestMapping("api/v1/order")
@Slf4j
public class OrderController {

    public final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PatchMapping("/{orderId}/cancel")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'MERCHANT')")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(
            @PathVariable("orderId") String orderId)
            throws NoSuchElementException {

        orderService.cancelOrder(orderId);
        ApiResponse<Void> apiResponse = ApiResponse.success(null);
        log.info("Customer cancel dish successfully");
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}

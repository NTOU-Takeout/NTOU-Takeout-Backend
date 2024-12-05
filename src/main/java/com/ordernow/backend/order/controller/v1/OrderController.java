package com.ordernow.backend.order.controller.v1;

import com.ordernow.backend.common.dto.ApiResponse;
import com.ordernow.backend.order.model.entity.OrderedStatus;
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
            throws NoSuchElementException, IllegalStateException {

        orderService.cancelOrder(orderId);
        ApiResponse<Void> apiResponse = ApiResponse.success(null);
        log.info("Cancel order successfully");
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PatchMapping("/{orderId}/accept")
    @PreAuthorize("hasAnyRole('MERCHANT')")
    public ResponseEntity<ApiResponse<Void>> acceptOrder(
            @PathVariable("orderId") String orderId)
            throws NoSuchElementException, IllegalStateException {

        orderService.updateStatus(orderId, OrderedStatus.PROCESSING);
        ApiResponse<Void> apiResponse = ApiResponse.success(null);
        log.info("Merchant accept order successfully");
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PatchMapping("/{orderId}/complete")
    @PreAuthorize("hasAnyRole('MERCHANT')")
    public ResponseEntity<ApiResponse<Void>> completeOrder(
            @PathVariable("orderId") String orderId)
            throws NoSuchElementException, IllegalStateException {

        orderService.updateStatus(orderId, OrderedStatus.COMPLETED);
        ApiResponse<Void> apiResponse = ApiResponse.success(null);
        log.info("Merchant complete order successfully");
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PatchMapping("/{orderId}/pickup")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<Void>> pickUpOrder(
            @PathVariable("orderId") String orderId)
            throws NoSuchElementException, IllegalStateException {

        orderService.updateStatus(orderId, OrderedStatus.PICKED_UP);
        ApiResponse<Void> apiResponse = ApiResponse.success(null);
        log.info("Customer picked up order successfully");
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}

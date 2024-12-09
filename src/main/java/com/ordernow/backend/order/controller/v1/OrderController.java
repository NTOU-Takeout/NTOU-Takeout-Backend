package com.ordernow.backend.order.controller.v1;

import com.ordernow.backend.auth.model.entity.CustomUserDetail;
import com.ordernow.backend.common.dto.ApiResponse;
import com.ordernow.backend.order.model.entity.Order;
import com.ordernow.backend.order.model.entity.OrderedStatus;
import com.ordernow.backend.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@RestController("CustomerControllerV1")
@RequestMapping("api/v1/order")
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private static final Set<String> ALLOWED_ORDER_STATUS = Set.of("PENDING", "PROCESSING", "COMPLETED", "PICKED_UP", "CANCELED");

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

    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<List<Order>>> filterOrder(
            @RequestParam(value="status") OrderedStatus status,
            @AuthenticationPrincipal CustomUserDetail customUserDetail)
            throws NoSuchElementException, IllegalStateException {

        System.out.println(status.toString());
        if(!ALLOWED_ORDER_STATUS.contains(status.toString())) {
            throw new IllegalArgumentException("Invalid order status");
        }

        List<Order> orderList = orderService.getOrderListByStatus(customUserDetail.getId(), status);
        ApiResponse<List<Order>> apiResponse = ApiResponse.success(orderList);
        log.info("Customer filter order successfully");
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}

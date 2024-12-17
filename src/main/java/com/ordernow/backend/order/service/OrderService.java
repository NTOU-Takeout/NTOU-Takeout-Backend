package com.ordernow.backend.order.service;

import com.ordernow.backend.auth.model.entity.CustomUserDetail;
import com.ordernow.backend.notification.model.dto.Notification;
import com.ordernow.backend.order.model.entity.Order;
import com.ordernow.backend.order.model.entity.OrderedStatus;
import com.ordernow.backend.order.repository.OrderRepository;
import com.ordernow.backend.user.model.entity.Merchant;
import com.ordernow.backend.user.model.entity.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;
    private static final Set<String> CUSTOMER_ALLOWED_TO_UPDATED_STATUS = Set.of("CANCELED", "PICKED_UP");
    private static final Set<String> MERCHANT_ALLOWED_TO_UPDATED_STATUS = Set.of("CANCELED", "PROCESSING", "COMPLETED");


    @Autowired
    public OrderService(OrderRepository orderRepository, ApplicationEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
    }

    public Order getOrderAndValid(String orderId)
            throws NoSuchElementException {

        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            throw new NoSuchElementException("Order not found with ID: " + orderId);
        }

        return order;
    }

    public void updateStatus(CustomUserDetail userDetail, String orderId, OrderedStatus status)
            throws NoSuchElementException, IllegalStateException { // Bug

        Order order = getOrderAndValid(orderId);
        Role role = userDetail.getRole();

        if(status == OrderedStatus.PENDING) {
            throw new IllegalStateException("Order status can not be PENDING");
        }
        if(status == OrderedStatus.CANCELED && order.getStatus() != OrderedStatus.PENDING) {
            throw new IllegalStateException("Order is not in PENDING status");
        }
        if(status == OrderedStatus.PROCESSING && order.getStatus() != OrderedStatus.PENDING) {
            throw new IllegalStateException("Order is not in PENDING status");
        }
        if(status == OrderedStatus.COMPLETED && order.getStatus() != OrderedStatus.PROCESSING) {
            throw new IllegalStateException("Order is not in PROCESSING status");
        }
        if(status == OrderedStatus.PICKED_UP && order.getStatus() != OrderedStatus.COMPLETED) {
            throw new IllegalStateException("Order is not in COMPLETED status");
        }

        if(role == Role.CUSTOMER && !CUSTOMER_ALLOWED_TO_UPDATED_STATUS.contains(status.toString())) {
            throw new IllegalStateException("Customer is not allowed to update status");
        }
        if(role == Role.MERCHANT && !MERCHANT_ALLOWED_TO_UPDATED_STATUS.contains(status.toString())) {
            throw new IllegalStateException("Merchant is not allowed to update status");
        }

        order.setStatus(status);
        if(status == OrderedStatus.PROCESSING) {
            order.setAcceptTime(LocalTime.now());
        }

        orderRepository.save(order);
        eventPublisher.publishEvent(
                new Notification(orderId,
                        order.getStatus(),
                        java.time.Instant.now().toString()
                )
        );
    }

    public List<Order> getOrderListByStatus(CustomUserDetail userDetail, OrderedStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        if(userDetail.getRole() == Role.CUSTOMER) {
            return orderRepository.findAllByCustomerIdAndStatus(userDetail.getId(), status, pageable);
        }
        if(userDetail.getRole() == Role.MERCHANT) {
            Merchant merchant = (Merchant) userDetail.getUser();
            return orderRepository.findAllByStoreIdAndStatus(merchant.getStoreId(), status, pageable);
        }
        return null;
    }
}

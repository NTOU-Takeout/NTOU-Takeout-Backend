package com.ordernow.backend.order.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Document(collection = "order")
public class Order {
    @Id
    private String id;
    private String customerId;
    private String storeId;
    private Double cost;
    private OrderedStatus status;
    private List<OrderedDish> orderedDishes;
    private LocalTime orderTime;
    private LocalTime acceptTime;
    private Integer estimatedPrepTime;

    public Order() {
        orderedDishes = new ArrayList<>();
    }

    public Order(String customerId) {
        this.customerId = customerId;
        cost = 0.0;
        orderTime = LocalTime.now();
        status = OrderedStatus.IN_CART;
        orderedDishes = new ArrayList<>();
    }
}

package com.ordernow.backend.dto.order;

import com.ordernow.backend.entity.order.Order;
import lombok.Data;

@Data
public class OrderResponse {
    private String id;
    private Double cost;

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.cost = order.getCost();
    }
}

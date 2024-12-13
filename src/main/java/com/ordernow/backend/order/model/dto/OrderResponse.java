package com.ordernow.backend.order.model.dto;

import com.ordernow.backend.order.model.entity.Order;
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

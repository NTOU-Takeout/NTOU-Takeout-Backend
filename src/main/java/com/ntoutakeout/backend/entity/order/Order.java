package com.ntoutakeout.backend.entity.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Document(collection = "order")
public class Order {
    @Id
    private String id;
    private String customerId;
    private Double cost;
    private LocalDateTime date;
    private OrderedStatus status;
    private String storeId;
    private List<OrderedDish> orderedDishes;

    public Order() {
        orderedDishes = new ArrayList<>();
    }

    public Order(String customerId) {
        this.customerId = customerId;
        cost = 0.0;
        date = LocalDateTime.now();
        status = OrderedStatus.IN_CART;
        orderedDishes = new ArrayList<>();
    }
}

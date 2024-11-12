package com.ntoutakeout.backend.entity.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Document(collection = "order")
public class Order {
    @Id
    private String id;
    private String costumerId;
    private Double cost;
    private LocalDateTime date;
    private OrderedStatus status;
    private String StoreId;
    private List<OrderedDish> orderedDishes;
}
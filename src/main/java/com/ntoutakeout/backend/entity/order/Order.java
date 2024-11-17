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
@NoArgsConstructor
@Document(collection = "order")
public class Order {
    @Id
    private String id;
    private String customerId;
    private Double cost;
    private LocalDateTime date;
    private OrderedStatus status;
    private String storeId;

    private List<OrderedDish> orderedDishes = new ArrayList<>();

    public void calculateTotalCost() {
        if (orderedDishes != null && !orderedDishes.isEmpty()) {
            this.cost = orderedDishes.stream()
                    .mapToDouble(dish -> {
                        double dishCost = dish.getPrice() != null ? dish.getPrice() * dish.getQuantity() : 0.0;
                        double attributesCost = dish.getChosenAttributes() != null ?
                                dish.getChosenAttributes().stream()
                                        .mapToDouble(attr -> attr.getExtraCost() != null ? attr.getExtraCost() : 0.0)
                                        .sum() : 0.0;
                        return dishCost + attributesCost;
                    })
                    .sum();
        } else {
            this.cost = 0.0;
        }
    }
}

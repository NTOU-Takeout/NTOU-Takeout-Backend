package com.ntoutakeout.backend.entity.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private String storeId;

    private List<OrderedDish> orderedDishes;

    public Order() {

    }

    public void calculateTotalCost() {
        if (orderedDishes != null && !orderedDishes.isEmpty()) {
            this.cost = orderedDishes.stream()
                    .mapToDouble(dish -> dish.getPrice() * dish.getQuantity()
                            + dish.getChosenAttributes().stream().mapToDouble(ChosenAttribute::getExtraCost).sum())
                    .sum();
        } else {
            this.cost = 0.0;
        }
    }

    public void setCustomerId(String customerId) {
        this.costumerId = customerId;
    }
}

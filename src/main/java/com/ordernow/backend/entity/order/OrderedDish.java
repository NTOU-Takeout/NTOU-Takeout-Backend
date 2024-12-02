package com.ordernow.backend.entity.order;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Data
@AllArgsConstructor
public class OrderedDish {
    private String id;
    private String storeId;
    private String dishId;
    private String dishName;
    private Double price;
    private Integer quantity;
    private String note;
    private List<ChosenAttribute> chosenAttributes;


    public OrderedDish() {
        this.id = UUID.randomUUID().toString();
        this.chosenAttributes = new ArrayList<>();
    }

    public boolean equalsWithoutId(OrderedDish o) {
        return Objects.equals(storeId, o.storeId)
                && Objects.equals(dishId, o.dishId)
                && Objects.equals(dishName, o.dishName)
                && Objects.equals(price, o.price)
                && Objects.equals(note, o.note)
                && Objects.equals(chosenAttributes, o.chosenAttributes);
    }
}

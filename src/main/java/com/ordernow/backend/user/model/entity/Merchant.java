package com.ordernow.backend.user.model.entity;

import com.ordernow.backend.auth.model.dto.RegisterRequest;
import com.ordernow.backend.order.model.entity.Order;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@Document(collection = "user")
@TypeAlias("merchant")
public class Merchant extends User{
    private String storeId;
    private List<Order> orderList;

    public Merchant() {
        orderList = new ArrayList<>();
    }

    public Merchant(RegisterRequest request, String storeId) {
        super(request);
        this.storeId = storeId;
        orderList = new ArrayList<>();
    }
}

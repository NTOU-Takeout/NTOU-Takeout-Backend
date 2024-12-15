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

@Getter
@Setter
@ToString
@Document(collection = "user")
@TypeAlias("customer")
public class Customer extends User{
    private List<String> storeCollection;
    private List<Order> orderList;

    public Customer() {
        super();
        storeCollection = new ArrayList<>();
        orderList = new ArrayList<>();
    }

    public Customer(RegisterRequest request) {
        super(request);
        storeCollection = new ArrayList<>();
        orderList = new ArrayList<>();
    }
}

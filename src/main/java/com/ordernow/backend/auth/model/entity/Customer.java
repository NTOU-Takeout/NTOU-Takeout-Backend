package com.ordernow.backend.auth.model.entity;

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
        storeCollection = new ArrayList<String>();
        orderList = new ArrayList<Order>();
    }
}

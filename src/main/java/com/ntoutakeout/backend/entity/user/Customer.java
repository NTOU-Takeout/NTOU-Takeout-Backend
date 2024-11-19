package com.ntoutakeout.backend.entity.user;

import com.ntoutakeout.backend.entity.Cart;
import com.ntoutakeout.backend.entity.order.Order;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class Customer extends User{
    private List<String> storeCollection;
    private List<Order> orderHistory;
}

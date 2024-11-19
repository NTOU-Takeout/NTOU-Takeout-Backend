package com.ntoutakeout.backend.entity.order;

import lombok.Getter;

@Getter
public enum OrderedStatus {
    IN_CART("訂單在購物車"),
    PENDING("顧客送出訂單，等待商家接單"),
    PROCESSING("商家已接單，開始製作餐點"),
    COMPLETED("商家已完成餐點"),
    PICKED_UP("顧客已領取餐點"),
    CANCELED("訂單已被取消");

    private final String description;

    OrderedStatus(String description) {
        this.description = description;
    }

}

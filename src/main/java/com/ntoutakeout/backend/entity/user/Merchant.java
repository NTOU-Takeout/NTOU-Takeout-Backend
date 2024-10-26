package com.ntoutakeout.backend.entity.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Merchant extends User{
    private String storeId;
}

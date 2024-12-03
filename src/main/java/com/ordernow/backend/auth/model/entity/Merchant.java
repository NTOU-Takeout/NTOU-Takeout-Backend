package com.ordernow.backend.auth.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@Document(collection = "user")
@TypeAlias("merchant")
public class Merchant extends User{
    private String storeId;
}

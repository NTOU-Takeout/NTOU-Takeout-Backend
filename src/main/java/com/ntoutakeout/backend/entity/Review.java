package com.ntoutakeout.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "review")
public class Review {
    @Id
    private String id;
    private String storeId;
    private String userId;
    private String userName;
    private String comment;
    private Double rating;
    private Date date;
}

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
    private Double averageSpend;
    private String comment;
    private String userId;
    private String userName;
    private Double rating;
    private Date date;
}

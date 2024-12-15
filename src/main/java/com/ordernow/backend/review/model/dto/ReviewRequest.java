package com.ordernow.backend.review.model.dto;

import lombok.Data;

@Data
public class ReviewRequest {
    private Double averageSpend;
    private String comment;
    private Double rating;
}

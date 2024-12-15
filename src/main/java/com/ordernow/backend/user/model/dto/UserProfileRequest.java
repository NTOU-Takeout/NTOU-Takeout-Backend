package com.ordernow.backend.user.model.dto;

import lombok.Data;

@Data
public class UserProfileRequest {
    private String name;
    private String phoneNumber;
    private String avatarUrl;
}

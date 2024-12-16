package com.ordernow.backend.user.model.dto;

import com.ordernow.backend.user.model.entity.Gender;
import lombok.Data;

@Data
public class UserProfileRequest {
    private String name;
    private String phoneNumber;
    private String avatarUrl;
    private Gender gender;
}

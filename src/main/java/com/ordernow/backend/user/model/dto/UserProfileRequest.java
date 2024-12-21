package com.ordernow.backend.user.model.dto;

import com.ordernow.backend.user.model.entity.Gender;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserProfileRequest {
    private String name;
    private String phoneNumber;
    private String avatarUrl;
    private Gender gender;

    public UserProfileRequest(String name, String phoneNumber, String avatarUrl, Gender gender) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.avatarUrl = avatarUrl;
        this.gender = gender;
    }
}

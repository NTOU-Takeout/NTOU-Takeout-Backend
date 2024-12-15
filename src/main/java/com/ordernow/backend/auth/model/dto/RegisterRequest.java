package com.ordernow.backend.auth.model.dto;

import com.ordernow.backend.user.model.entity.Gender;
import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private Gender gender;
}

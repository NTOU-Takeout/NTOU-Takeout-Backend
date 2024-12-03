package com.ordernow.backend.dto.auth;

import com.ordernow.backend.entity.user.Gender;
import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private Gender gender;
}

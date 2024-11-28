package com.ntoutakeout.backend.dto.auth;

import com.ntoutakeout.backend.entity.user.Gender;
import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private Gender gender;
}

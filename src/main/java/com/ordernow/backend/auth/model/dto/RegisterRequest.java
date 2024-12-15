package com.ordernow.backend.auth.model.dto;

import com.ordernow.backend.user.model.entity.Gender;
import com.ordernow.backend.user.model.entity.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String avatarUrl;
    private Gender gender;
    private Role role;
}

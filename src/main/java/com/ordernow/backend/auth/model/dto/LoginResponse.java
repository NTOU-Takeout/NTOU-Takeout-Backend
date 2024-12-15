package com.ordernow.backend.auth.model.dto;

import com.ordernow.backend.user.model.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String id;
    private String name;
    private String email;
    private String avatarUrl;
    private Role role;
    private String token;
}

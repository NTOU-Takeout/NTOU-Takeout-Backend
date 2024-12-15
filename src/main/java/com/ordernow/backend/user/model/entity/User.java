package com.ordernow.backend.user.model.entity;

import com.ordernow.backend.auth.model.dto.RegisterRequest;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "user")
public class User {
    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String avatarUrl;
    private Gender gender;
    private Role role;

    public User(RegisterRequest user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.phoneNumber = user.getPhoneNumber();
        this.avatarUrl = user.getAvatarUrl();
        this.gender = user.getGender();
        this.role = user.getRole();
    }
}
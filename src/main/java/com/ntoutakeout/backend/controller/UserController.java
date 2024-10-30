package com.ntoutakeout.backend.controller;

import com.ntoutakeout.backend.entity.Store;
import com.ntoutakeout.backend.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController()
@RequestMapping("/api/user")
@Slf4j
public class UserController {
    @Autowired
    private StoreService userService;

    public class UserSignupRequest {
        private String name;
        private String email;
        private String password;
        private String phoneNumber;
        private String gender;
        private String role;
    }
    public class UserLoginRequest {
        private String email;
        private String password;
    }
    public class UserLoginResponse {
        private String jwtToken;
        private String role;

    }

    @GetMapping("/getUserIdByEmail")
    public ResponseEntity<String> getIdList(
            @RequestParam(value = "email", required = true, defaultValue = "") String email) {

        log.info("Fetch API: getUserIdByEmail Success");
        String userId = userService.getUserIdByEmail(email);

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(userId);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUpUser(@RequestBody UserSignupRequest userSignupRequest) {
        try {
            String userId = userService.signUpUser(userSignupRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(userId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> loginUser(@RequestBody UserLoginRequest userLoginRequest) {
        try {
            UserLoginResponse loginResponse = userService.loginUser(userLoginRequest);
            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
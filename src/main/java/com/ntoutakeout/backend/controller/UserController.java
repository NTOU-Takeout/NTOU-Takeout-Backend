package com.ntoutakeout.backend.controller;

import com.ntoutakeout.backend.entity.Store;
import com.ntoutakeout.backend.entity.user.User;
import com.ntoutakeout.backend.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/api/user")
@Slf4j
public class UserController {
    @Autowired
    private StoreService userService;

    @GetMapping("/getUserIdByEmail")
    public ResponseEntity<?> getUserIdByEmail(@RequestParam("email") String email) {
        try {
            User user = userService.findByEmail(email);

            Map<String, Object> response = new HashMap<>();
            response.put("User entity", user);

            if (user == null) {
                log.error("User not found with email: " + email);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error fetching user by email: " + email, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }


    @PostMapping("/signup")
    public ResponseEntity<?> signUpUser(@RequestBody User user) {
        try {
            User newUser = userService.signUpUser(user);

            String token = userService.generateToken(newUser);

            Map<String, Object> response = new HashMap<>();
            response.put("User entity", newUser);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .header("Authorization", "Bearer " + token)
                    .body(response);
        } catch (Exception e) {
            log.error("Signup failed for user: " + .getEmail(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Signup failed: " + e.getMessage());
        }
    }




    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        try {
            User loginUser = userService.loginUser(user);

            String token = userService.generateToken(user);

            Map<String, Object> response = new HashMap<>();
            response.put("User entity", loginUser);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .header("Authorization", "Bearer " + token)
                    .body(response);
        } catch (Exception e) {
            log.error("Login failed for email: " + user.getEmail(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: " + e.getMessage());
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateUser(@RequestHeader("Authorization") String token,
                                        @RequestBody Map<String, Object> userUpdates) {
        try {

            String jwtToken = token.replace("Bearer ", "");
            Claims claims = validateToken(jwtToken);

            String name = (String) userUpdates.get("name");
            String password = (String) userUpdates.get("password");
            String phoneNumber = (String) userUpdates.get("phoneNumber");

            User updatedUser = userService.updateUser(name, password, phoneNumber);

            if (updatedUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found or update failed.");
            }

            Map<String, Object> response = new HashMap<>();
            response.put("User entity", updatedUser);

            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + jwtToken)
                    .body(response);
        } catch (Exception e) {
            log.error("Error updating user: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update user.");
        }
    }



}
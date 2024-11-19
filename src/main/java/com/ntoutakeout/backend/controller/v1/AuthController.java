package com.ntoutakeout.backend.controller.v1;

import com.ntoutakeout.backend.dto.LoginRequest;
import com.ntoutakeout.backend.entity.user.User;
import com.ntoutakeout.backend.service.AuthService;
import com.ntoutakeout.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @Autowired
    public AuthController(AuthService userService, UserService authService) {
        this.authService = userService;
        this.userService = authService;
    }

//    @GetMapping("/getUserIdByEmail")
//    public ResponseEntity<?> getUserIdByEmail(@RequestParam("email") String email) {
//        log.info("Fetch API: getUserIdByEmail Success");
//        try {
//            User user = userService.findByEmail(email);
//
//            if (user == null) {
//                log.error("User not found with email: " + email);
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
//            }
//
//            return ResponseEntity.ok(user);
//        } catch (Exception e) {
//            log.error("Error fetching user by email: " + email, e);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
//        }
//    }


    @PostMapping("/register")
    public ResponseEntity<String> signUpUser(@RequestBody User user) {
        log.info("Fetch API: register Success");
        try {
            authService.createUser(user);
            return ResponseEntity.status(HttpStatus.OK).body("Success");
        } catch (Exception e) {
            log.error("Signup failed for user: {}", user.getEmail(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            String token = authService.verify(loginRequest);
            User user = userService.getUserByEmail(loginRequest.getEmail());
            log.info("Fetch API: login Success");
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Authorization", "Bearer " + token)
                    .body(user.getId());
        } catch (Exception e) {
            log.error("Login failed for email: {}", loginRequest.getEmail(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: " + e.getMessage());
        }
    }
//
//    @PatchMapping("/update")
//    public ResponseEntity<User> updateUser(@RequestHeader("Authorization") String token,
//                                           @RequestBody Map<String, Object> userUpdates) {
//        log.info("Fetch API: update User");
//        try {
//            String jwtToken = token.replace("Bearer ", "");
//            Claims claims = validateToken(jwtToken);
//            if (claims == null) {
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//            }
//
//            User updatedUser = userService.updateUser(userUpdates);
//
//            if (updatedUser == null) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//            }
//
//            return ResponseEntity.ok(updatedUser);
//
//        } catch (IllegalArgumentException e) {
//            log.error("Update failed due to invalid input: ", e);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        } catch (SecurityException e) {
//            log.error("Update failed due to authorization: ", e);
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        } catch (Exception e) {
//            log.error("Unexpected error updating user: ", e);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        }
//    }
}

package com.ntoutakeout.backend.controller.v2;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.ntoutakeout.backend.dto.ApiResponse;
import com.ntoutakeout.backend.dto.auth.LoginRequest;
import com.ntoutakeout.backend.entity.user.Customer;
import com.ntoutakeout.backend.entity.user.User;
import com.ntoutakeout.backend.service.AuthService;
import com.ntoutakeout.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController("AuthControllerV2")
@RequestMapping("/api/v2/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final FirebaseAuth firebaseAuth;

    @Autowired
    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
        this.firebaseAuth = FirebaseAuth.getInstance();
    }
    
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> signUpUser(
            @RequestBody Customer user)
            throws IllegalArgumentException {

        authService.createUser(user);
        ApiResponse<Void> apiResponse = ApiResponse.success(null);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<HashMap<String, String>>> loginUser(
            @RequestBody LoginRequest loginRequest)
            throws AuthenticationServiceException {

        String token = authService.verify(loginRequest);
        User user = userService.getUserByEmail(loginRequest.getEmail());
        HashMap<String, String> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("token", token);
        ApiResponse<HashMap<String, String>> apiResponse = ApiResponse.success(response);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
    

}

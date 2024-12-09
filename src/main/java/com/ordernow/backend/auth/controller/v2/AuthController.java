package com.ordernow.backend.auth.controller.v2;

import com.ordernow.backend.auth.model.dto.LoginResponse;
import com.ordernow.backend.common.dto.ApiResponse;
import com.ordernow.backend.auth.model.dto.LoginRequest;
import com.ordernow.backend.auth.model.entity.Customer;
import com.ordernow.backend.auth.model.entity.User;
import com.ordernow.backend.auth.service.AuthService;
import com.ordernow.backend.auth.service.UserService;
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

    @Autowired
    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
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
    public ResponseEntity<ApiResponse<LoginResponse>> loginUser(
            @RequestBody LoginRequest loginRequest)
            throws AuthenticationServiceException {

        String token = authService.verify(loginRequest);
        User user = userService.getUserByEmail(loginRequest.getEmail());
        LoginResponse response = new LoginResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAvatarUrl(),
                user.getRole(),
                token
        );
        ApiResponse<LoginResponse> apiResponse = ApiResponse.success(response);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}

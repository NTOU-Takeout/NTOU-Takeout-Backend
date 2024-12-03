package com.ordernow.backend.auth.controller.v1;

import com.ordernow.backend.common.dto.ApiResponse;
import com.ordernow.backend.auth.model.entity.User;
import com.ordernow.backend.auth.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("AdminControllerV1")
@RequestMapping("/api/v1/admin")
@Slf4j
public class AdminController {
    private final AuthService userService;

    @Autowired
    public AdminController(AuthService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {

        List<User> users = userService.getAllUsers();
        ApiResponse<List<User>> apiResponse = ApiResponse.success(users);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }
}

package com.ordernow.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EnableMethodSecurity
@SpringBootApplication
public class OrderNowBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderNowBackendApplication.class, args);
    }

}

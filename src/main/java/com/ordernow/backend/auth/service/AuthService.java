package com.ordernow.backend.auth.service;

import com.ordernow.backend.auth.model.dto.LoginRequest;
import com.ordernow.backend.user.model.entity.Customer;
import com.ordernow.backend.user.model.entity.Merchant;
import com.ordernow.backend.user.model.entity.User;
import com.ordernow.backend.auth.repository.UserRepository;
import com.ordernow.backend.security.jwt.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authManager;
    private final JWTService jwtService;
    private final static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    public AuthService(UserRepository userRepository,
                       AuthenticationManager authManager,
                       JWTService jwtService) {
        this.userRepository = userRepository;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    public void validateEmail(String email) {
        if(userRepository.findByEmail(email) != null) {
            throw new IllegalArgumentException("Email already exists");
        }
    }

    public void validateName(String name) {
        if(name.length() > 20) {
            throw new IllegalArgumentException("Name is too long");
        }
    }

    public void createUser(User user)
            throws IllegalArgumentException {

        validateEmail(user.getEmail());
        validateName(user.getName());
        user.setPassword(encoder.encode(user.getPassword()));
        User savedUser = switch(user.getRole()) {
            case CUSTOMER -> new Customer(user);
            case MERCHANT -> new Merchant(user);
            default -> throw new IllegalArgumentException("Invalid role");
        };
        userRepository.save(savedUser);
    }

    public String verify(LoginRequest loginRequest)
            throws AuthenticationServiceException {

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        if(!authentication.isAuthenticated()) {
            throw new AuthenticationServiceException("Authentication failed");
        }
        return jwtService.generateToken(loginRequest.getEmail());
    }
}

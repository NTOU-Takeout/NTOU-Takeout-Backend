package com.ntoutakeout.backend.service;

import com.google.firebase.auth.FirebaseToken;
import com.ntoutakeout.backend.dto.auth.LoginRequest;
import com.ntoutakeout.backend.entity.user.Customer;
import com.ntoutakeout.backend.entity.user.User;
import com.ntoutakeout.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void createUser(Customer user)
            throws IllegalArgumentException {
        if(userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email already exists");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
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

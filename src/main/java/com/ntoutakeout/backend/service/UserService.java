package com.ntoutakeout.backend.service;

import com.ntoutakeout.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class UserService {
    @Autowired
    private UserRepository userRepository;

}

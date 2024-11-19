package com.ntoutakeout.backend.config;

import com.ntoutakeout.backend.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider extends DaoAuthenticationProvider {


    @Autowired
    public CustomAuthenticationProvider(CustomUserDetailsService userDetailsService) {
        setUserDetailsService(userDetailsService);
        setPasswordEncoder(new BCryptPasswordEncoder(12));
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        super.additionalAuthenticationChecks(userDetails, authentication);
    }
}
package com.grahql.example.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class ProblemzHttpConfigurer extends AbstractHttpConfigurer<ProblemzHttpConfigurer, HttpSecurity> {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        var authenticationManager = http.getSharedObject(AuthenticationManager.class);
        http.addFilterBefore(
                new ProblemzSecurityFilter(authenticationManager),
                UsernamePasswordAuthenticationFilter.class
        );
    }

    public static ProblemzHttpConfigurer newInstance() {
        return new ProblemzHttpConfigurer();
    }

}
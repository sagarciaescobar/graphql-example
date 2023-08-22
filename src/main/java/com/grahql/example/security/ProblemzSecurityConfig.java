package com.grahql.example.security;

import com.grahql.example.datasource.problemz.repository.UserzRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class ProblemzSecurityConfig {

    @Autowired
    private UserzRepository userzRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        var authProvider = new ProblemzAuthenticationProvider(userzRepository);

        http.apply(ProblemzHttpConfigurer.newInstance());
        http.authenticationProvider(authProvider);
        http.csrf().disable().authorizeHttpRequests(
                auth -> auth.anyRequest().authenticated()
        );

        return http.build();
    }

}
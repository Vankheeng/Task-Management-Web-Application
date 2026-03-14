package com.myapplication.taskmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for testing with Postman
                .authorizeHttpRequests(auth -> auth
                        // Allow anyone to hit the POST /user endpoint (registration)
                        .requestMatchers(HttpMethod.POST, "/user").permitAll()
                        // Everything else still requires login
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
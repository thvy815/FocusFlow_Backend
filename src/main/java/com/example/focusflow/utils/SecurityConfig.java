package com.example.focusflow.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

        @Autowired
        private JwtFilter jwtFilter;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                        .csrf(csrf -> csrf.disable()) // Tắt CSRF nếu không cần thiết lập bảo mật CSRF
                        .sessionManagement(session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .authorizeHttpRequests(authz -> authz
                                .requestMatchers("/auth/**", "/ws/**").permitAll() // Cho phép truy cập mà không cần xác thực
                                .requestMatchers("/api/pro/**").authenticated()
                                .anyRequest().authenticated())
                        .httpBasic(basic -> basic.disable()) // Tắt xác thực cơ bản (basic auth) vì bạn đang
                        .formLogin(login -> login.disable()); // Không cần form login cho API

                // Thêm JwtAuthenticationFilter trước khi kiểm tra
                // UsernamePasswordAuthentication
                http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}

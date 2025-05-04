package com.example.focusflow.controller;

import com.example.focusflow.entity.User;
import com.example.focusflow.model.SignInRequest;
import com.example.focusflow.model.SignInResponse;
import com.example.focusflow.service.UserService;
import com.example.focusflow.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody SignInRequest request) {
        User user = userService.getUserByEmail(request.getEmail());
        if (user != null && userService.checkPassword(user, request.getPassword())) {
            String token = jwtUtil.generateToken(user.getEmail());
            SignInResponse response = new SignInResponse(token);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
}

package com.example.focusflow.controller;

import com.example.focusflow.entity.User;
import com.example.focusflow.model.GoogleLoginRequest;
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

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import java.util.Collections;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/signin")
    public ResponseEntity<?> login(@RequestBody SignInRequest request) {
        // Cho phép login bằng username hoặc email
        User user = userService.getUserByUsernameOrEmail(request.getEmailOrUsername());

        if (user != null && userService.checkPassword(user, request.getPassword())) {
            String token = jwtUtil.generateToken(user.getEmail(), request.getRememberMe());
            
            SignInResponse response = new SignInResponse(token, user.getId());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
    
    @PostMapping("/google-signin")
    public ResponseEntity<?> loginWithGoogle(@RequestBody GoogleLoginRequest request) {
        try {
            String idTokenString = request.getIdToken();

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList("410988307717-aitcia4ljjebo74lhehmj7t9ol6tuhmo.apps.googleusercontent.com"))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken != null) {
                Payload payload = idToken.getPayload();
                String email = payload.getEmail();
                String fullName = (String) payload.get("name");
                String avatarUrl = (String) payload.get("picture");

                User user = userService.getUserByEmail(email);
                if (user == null) {
                    user = new User();
                    user.setFullName(fullName);
                    user.setEmail(email);
                    user.setUsername(generateUsernameFromEmail(email)); // Tạo username từ email
                    user.setProvider("google");
                    user.setAvatarUrl(avatarUrl);
                    user.setPassword(UUID.randomUUID().toString()); // Tạo mật khẩu ngẫu nhiên
                    userService.saveUser(user);
                }

                 // Tạo JWT token và trả về
                String token = jwtUtil.generateToken(email, false); 
                return ResponseEntity.ok(new SignInResponse(token, user.getId()));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid ID token");
            }
        } catch (Exception e) {
            e.printStackTrace(); 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Google login error: " + e.getMessage());
        }
    }

    private String generateUsernameFromEmail(String email) {
        return email.split("@")[0];
    }
}

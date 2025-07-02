package com.example.focusflow.controller;

import com.example.focusflow.entity.PasswordResetToken;
import com.example.focusflow.entity.User;
import com.example.focusflow.model.GoogleLoginRequest;
import com.example.focusflow.model.SignInRequest;
import com.example.focusflow.model.SignInResponse;
import com.example.focusflow.model.SignUpRequest;
import com.example.focusflow.repository.PasswordResetTokenRepository;
import com.example.focusflow.service.EmailService;
import com.example.focusflow.service.UserService;
import com.example.focusflow.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${app.reset-password-url}")
    private String resetPasswordUrl;

    @PostMapping("/signup")
    public ResponseEntity<User> createUser(@RequestBody SignUpRequest request) {
        User user = new User();
        user.setFullName(request.getFullName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); 
        user.setProvider("local");

        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

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

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        User user = userService.getUserByEmail(email);
        if (user == null) return ResponseEntity.badRequest().body("Email không tồn tại.");

        // Xoá token cũ nếu có
        Optional<PasswordResetToken> existingToken = passwordResetTokenRepository.findByUserId(user.getId());
        existingToken.ifPresent(passwordResetTokenRepository::delete);

        // Tạo token mới
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        passwordResetTokenRepository.save(resetToken);

        String resetLink = resetPasswordUrl + "?token=" + token;
        String html = "<p>Hello,</p>" +
              "<p>You have requested to reset your password. Please click the link below to proceed:</p>" +
              "<p><a href=\"" + resetLink + "\" target=\"_blank\">Reset your password</a></p>" +
              "<br><p>If you did not request this, please ignore this email.</p>";
        emailService.send(email, "Password Reset Request", html);

        return ResponseEntity.ok("Email đã được gửi.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        Optional<PasswordResetToken> tokenOptional = passwordResetTokenRepository.findByToken(token);
        if (tokenOptional.isEmpty()) return ResponseEntity.badRequest().body("Token không hợp lệ.");

        PasswordResetToken resetToken = tokenOptional.get();
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Token đã hết hạn.");
        }

        Optional<User> optionalUser = userService.getUserById(resetToken.getUser().getId());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body("Người dùng không tồn tại.");
        }
        User user = optionalUser.get();
        user.setPassword(newPassword);
        userService.saveUser(user);

        passwordResetTokenRepository.delete(resetToken); // chỉ dùng 1 lần
        return ResponseEntity.ok("Mật khẩu đã được cập nhật.");
    }
}
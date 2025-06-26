package com.example.focusflow.controller;

import com.example.focusflow.entity.User;
import com.example.focusflow.model.ProUpgradeRequest;
import com.example.focusflow.service.ProService;
import com.example.focusflow.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/pro") // Đường dẫn chung cho các API Pro
public class ProController {

    @Autowired
    private ProService proService;

    @Autowired
    private UserService userService;

    // API nâng cấp Pro (lấy email từ JWT)
    @PostMapping("/upgrade")
    public ResponseEntity<String> upgradePro(@RequestBody ProUpgradeRequest request) {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean success = proService.upgradeUserPro(email, request);
        if (success) {
            return ResponseEntity.ok("Upgrade successful");
        } else {
            return ResponseEntity.badRequest().body("Upgrade failed");
        }
    }

    @GetMapping("/status")
    public ResponseEntity<?> getProStatus() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getPrincipal() == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String email = (String) auth.getPrincipal();
        User user = proService.getProStatus(email);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        // ⚠ Kiểm tra expireTime
        Long now = System.currentTimeMillis();
        boolean isStillPro = user.getExpireTime() != null && user.getExpireTime() > now;

        // Nếu đã hết hạn → cập nhật lại trạng thái isPro = false
        if (!isStillPro && Boolean.TRUE.equals(user.getIsPro())) {
            user.setIsPro(false);
            user.setPlanName(null);
            user.setExpireTime(null);
            userService.saveUser(user); // Gọi lại repo để lưu
        }

        Map<String, Object> response = new HashMap<>();
        response.put("isPro", isStillPro);
        response.put("planName", user.getPlanName());
        response.put("expireTime", user.getExpireTime());

        return ResponseEntity.ok(response);
    }

}

package com.example.focusflow.controller;

import com.example.focusflow.model.ProUpgradeRequest;
import com.example.focusflow.entity.User;
import com.example.focusflow.service.ProService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pro")
public class ProController {

    @Autowired
    private ProService proService;

    @PostMapping("/upgrade")
    public ResponseEntity<?> upgradePro(@RequestBody ProUpgradeRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername(); // lấy từ JWT
        proService.upgradeUserPro(email, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/status")
    public ResponseEntity<?> getStatus(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername(); // lấy từ JWT
        User user = proService.getProStatus(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new ProStatusResponse(
                user.getIsPro(),
                user.getPlanName(),
                user.getExpireTime()));
    }

    // Inner response class
    public static class ProStatusResponse {
        public Boolean isPro;
        public String planName;
        public Long expireTime;

        public ProStatusResponse(Boolean isPro, String planName, Long expireTime) {
            this.isPro = isPro;
            this.planName = planName;
            this.expireTime = expireTime;
        }
    }
}

package com.example.focusflow.controller;


import com.example.focusflow.entity.Streak;
import com.example.focusflow.service.StreakService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/streak")
public class StreakController {
    private final StreakService streakService;

    public StreakController(StreakService streakService) {
        this.streakService = streakService;

    }

    // ✅ API: Lấy streak hiện tại của user
    @GetMapping("/{userId}")
    public ResponseEntity<Streak> getStreakByUser(@PathVariable Integer userId) {
        Streak streak = streakService.getStreakByUserId(userId);
        if (streak == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(streak);
    }

    // ✅ (Tuỳ chọn) API: reset streak (cho dev, test hoặc người dùng muốn reset)
    @PostMapping("/{userId}/reset")
    public ResponseEntity<Void> resetStreak(@PathVariable Integer userId) {
        streakService.resetStreak(userId);
        return ResponseEntity.ok().build();
    }
}
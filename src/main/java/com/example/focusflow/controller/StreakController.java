package com.example.focusflow.controller;


import com.example.focusflow.entity.Streak;
import com.example.focusflow.service.StreakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/streak")
public class StreakController {

    
    private final StreakService streakService;

    public StreakController(StreakService streakService) {
        this.streakService = streakService;
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateStreak(@RequestParam Integer userId) {
        Streak result = streakService.updateStreakIfUserCompletedToday(userId);
        if (result == null) {
            return ResponseEntity.ok("User has not completed any task today. Streak not updated.");
        }
        return ResponseEntity.ok(result);
    }
    @GetMapping("/dates")
public ResponseEntity<?> getStreakDates(@RequestParam Integer userId) {
    List<String> dates = streakService.getCompletedStreakDates(userId); // hoặc kiểu LocalDate nếu bạn muốn
    return ResponseEntity.ok(dates);
}
}
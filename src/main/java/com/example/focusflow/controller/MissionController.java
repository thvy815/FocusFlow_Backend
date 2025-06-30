package com.example.focusflow.controller;

import com.example.focusflow.service.MissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mission")
public class MissionController {

    private final MissionService missionService;

    public MissionController(MissionService missionService) {
        this.missionService = missionService;
    }

    @PostMapping("/evaluate/{userId}")
    public ResponseEntity<String> evaluateMission(@PathVariable Integer userId) {
        int gainedScore = missionService.evaluateAndUpdateScore(userId);
        return ResponseEntity.ok("Mission evaluated. Gained score: " + gainedScore);
    }
}
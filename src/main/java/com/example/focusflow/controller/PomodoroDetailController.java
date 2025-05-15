package com.example.focusflow.controller;

import com.example.focusflow.entity.PomodoroDetail;
import com.example.focusflow.service.PomodoroDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pomodoro-details")
public class PomodoroDetailController {

    private final PomodoroDetailService detailService;

    public PomodoroDetailController(PomodoroDetailService detailService) {
        this.detailService = detailService;
    }

    @PostMapping
    public ResponseEntity<PomodoroDetail> create(@RequestBody PomodoroDetail detail) {
        PomodoroDetail saved = detailService.create(detail);
        return ResponseEntity.ok(saved);
    }
}

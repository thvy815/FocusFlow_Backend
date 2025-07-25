package com.example.focusflow.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.focusflow.service.PomodoroDetailService;

import com.example.focusflow.entity.PomodoroDetail;

@RestController
@RequestMapping("/api/pomodoroDetails")
public class PomodoroDetailController {
    private final PomodoroDetailService pomodoroDetailService;

    public PomodoroDetailController(PomodoroDetailService pomodoroDetailService) {
        this.pomodoroDetailService = pomodoroDetailService;
    }

    @GetMapping("/pomodoros/{pomodoroId}")
    public List<PomodoroDetail> getTasksByUser(@PathVariable Integer pomodoroId) {
        return pomodoroDetailService.getAllPomodoroByPomodoroId(pomodoroId);
    }

    @GetMapping("/{id}")
    public Optional<PomodoroDetail> getPomodoroDetailById(@PathVariable Integer id) {
        return pomodoroDetailService.getPomodoroDetailById(id);
    }

    @PostMapping
    public PomodoroDetail createPomodoroDetail(@RequestBody PomodoroDetail pomodoroDetail) {
        return pomodoroDetailService.createPomodoroDetail(pomodoroDetail);
    }

    @DeleteMapping("/pomodoro/{pomodoroId}")
    public ResponseEntity<Void> deleteByPomodoroId(@PathVariable Integer pomodoroId) {
        pomodoroDetailService.deletePomodoroDetail(pomodoroId);
        return ResponseEntity.noContent().build();
    }
}

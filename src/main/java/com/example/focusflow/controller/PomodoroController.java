package com.example.focusflow.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.focusflow.entity.Pomodoro;
import com.example.focusflow.model.PomodoroRequest;
import com.example.focusflow.service.PomodoroService;

@RestController
@RequestMapping("/api/pomodoros")
public class PomodoroController {
    private final PomodoroService pomoService;

    public PomodoroController(PomodoroService pomoService) {
        this.pomoService = pomoService;
    }

    @PostMapping
    public ResponseEntity<Pomodoro> create(@RequestBody PomodoroRequest request) {
        return ResponseEntity.ok(pomoService.createPomodoro(request));
    }

    @GetMapping
    public List<Pomodoro> getAll() {
        return pomoService.getAllPomodoros();
    }

    @GetMapping("/{id}")
    public Pomodoro getById(@PathVariable Long id) {
        return pomoService.getPomodoroById(id);
    }

}

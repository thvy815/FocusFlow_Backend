package com.example.focusflow.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.focusflow.service.PomodoroService;

import com.example.focusflow.entity.Pomodoro;

@RestController
@RequestMapping("/api/pomodoro")
public class PomodoroController {
    private final PomodoroService pomodoroService;

    public PomodoroController(PomodoroService pomodoroService) {
        this.pomodoroService = pomodoroService;
    }

    @GetMapping("/user/{userId}")
    public List<Pomodoro> getPomodoroByUser(@PathVariable Integer userId) {
        return pomodoroService.getAllPomodoroByUserId(userId);
    }

    @GetMapping("/tasks/{taskId}")
    public List<Pomodoro> getPomodoroByTasks(@PathVariable Integer taskId) {
        return pomodoroService.getAllPomodoroByTaskId(taskId);
    }

    @GetMapping("/{id}")
    public Optional<Pomodoro> getTaskById(@PathVariable Integer id) {
        return pomodoroService.getPomodoroById(id);
    }

    @PostMapping
    public Pomodoro createPomodoro(@RequestBody Pomodoro pomodoro) {
        return pomodoroService.createPomodoro(pomodoro);
    }

    @DeleteMapping("/{id}")
    public void deletePomodoro(@PathVariable Integer id) {
        pomodoroService.deletePomodoro(id);
    }
}

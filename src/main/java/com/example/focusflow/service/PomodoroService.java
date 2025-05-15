package com.example.focusflow.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.focusflow.entity.Pomodoro;
// import com.example.focusflow.entity.User;
import com.example.focusflow.model.PomodoroRequest;

@Service
public interface PomodoroService {
    Pomodoro createPomodoro(PomodoroRequest request);

    List<Pomodoro> getAllPomodoros();

    Pomodoro getPomodoroById(Long id);
}

package com.example.focusflow.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.focusflow.repository.PomodoroRepository;
import com.example.focusflow.entity.Pomodoro;

@Service
public class PomodoroService {
    private final PomodoroRepository pomodoroRepository;

    public PomodoroService(PomodoroRepository pomodoroRepository) {
        this.pomodoroRepository = pomodoroRepository;
    }

    public List<Pomodoro> getAllPomodoroByUserId(Integer userId) {
        return pomodoroRepository.findByUserIdAndIsDeletedFalse(userId);
    }

    public Optional<Pomodoro> getPomodoroById(Integer id) {
        return pomodoroRepository.findById(id);
    }

    public Pomodoro createPomodoro(Pomodoro pomodoro) {
        return pomodoroRepository.save(pomodoro);
    }

    public void deletePomodoro(Integer id) {
        pomodoroRepository.findById(id).ifPresent(pomodoro -> {
            pomodoro.setDeleted(true);
            pomodoroRepository.save(pomodoro);
        });
    }
}

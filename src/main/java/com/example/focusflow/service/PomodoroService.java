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
        pomodoroRepository.deleteById(id);
    }

    public Pomodoro updatePomodoro(Pomodoro pomodoro) {
        Optional<Pomodoro> existing = pomodoroRepository.findById(pomodoro.getID());
        if (existing.isPresent()) {
            return pomodoroRepository.save(pomodoro);
        } else {
            throw new RuntimeException("Pomodoro not found with ID: " + pomodoro.getID());
        }
    }
}

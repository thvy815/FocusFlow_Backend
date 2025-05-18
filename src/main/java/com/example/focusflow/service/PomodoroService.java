package com.example.focusflow.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.focusflow.repository.PomodoroRepository;
import com.example.focusflow.entity.Pomodoro;
import com.example.focusflow.entity.Task;

@Service
public class PomodoroService {
    private final PomodoroRepository pomodoroRepository;

    public PomodoroService(PomodoroRepository pomodoroRepository) {
        this.pomodoroRepository = pomodoroRepository;
    }

    public List<Pomodoro> getAllPomodoroByUserId(Integer userId) {
        return pomodoroRepository.findByUserIdAndIsDeletedFalse(userId);
    }

    public List<Pomodoro> getAllPomodoroByTaskId(Integer taskId) {
        return pomodoroRepository.findByTaskIdAndIsDeletedFalse(taskId);
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

    public Pomodoro updatePomodoro(Pomodoro pomodoro) {
        return pomodoroRepository.save(pomodoro);
    }

}

package com.example.focusflow.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.focusflow.entity.Pomodoro;
import com.example.focusflow.model.PomodoroRequest;
import com.example.focusflow.repository.PomodoroRepository;

@Service
public class PomodoroServiceImpl implements PomodoroService {

    private final PomodoroRepository pomodoroRepository;

    public PomodoroServiceImpl(PomodoroRepository pomodoroRepository) {
        this.pomodoroRepository = pomodoroRepository;
    }

    @Override
    public Pomodoro createPomodoro(PomodoroRequest request) {
        Pomodoro pomodoro = new Pomodoro();

        return pomodoroRepository.save(pomodoro);
    }

    @Override
    public List<Pomodoro> getAllPomodoros() {
        return pomodoroRepository.findAll();
    }

    @Override
    public Pomodoro getPomodoroById(Long id) {
        return pomodoroRepository.findById(id).orElse(null);
    }
}

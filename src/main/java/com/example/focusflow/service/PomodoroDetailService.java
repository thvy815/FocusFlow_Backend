package com.example.focusflow.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.focusflow.repository.PomodoroDetailRepository;
import com.example.focusflow.controller.PomodoroDetailController;
import com.example.focusflow.entity.PomodoroDetail;

@Service
public class PomodoroDetailService {

    private final PomodoroDetailController pomodoroDetailController;
    private final PomodoroDetailRepository pomodoroDetailRepository;

    public PomodoroDetailService(PomodoroDetailRepository pomodoroDetailRepository,
            PomodoroDetailController pomodoroDetailController) {
        this.pomodoroDetailRepository = pomodoroDetailRepository;
        this.pomodoroDetailController = pomodoroDetailController;
    }

    public List<PomodoroDetail> getAllPomodoroByPomodoroId(Integer pomodoroId) {
        return pomodoroDetailRepository.findByPomodoroId(pomodoroId);
    }

    public Optional<PomodoroDetail> getPomodoroDetailById(Integer id) {
        return pomodoroDetailRepository.findById(id);
    }

    public PomodoroDetail createPomodoroDetail(PomodoroDetail pomodoroDetail) {
        return pomodoroDetailRepository.save(pomodoroDetail);
    }

    public void deletePomodoroDetail(Integer id) {
        pomodoroDetailRepository.findById(id).ifPresent(pomodoroDetail -> {
            pomodoroDetail.setDeleted(true);
            pomodoroDetailRepository.save(pomodoroDetail);
        });
    }
}

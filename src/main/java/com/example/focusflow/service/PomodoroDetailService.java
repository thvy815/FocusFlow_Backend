package com.example.focusflow.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.focusflow.repository.PomodoroDetailRepository;

import org.springframework.transaction.annotation.Transactional;

import com.example.focusflow.entity.PomodoroDetail;

@Service
public class PomodoroDetailService {

    private final PomodoroDetailRepository pomodoroDetailRepository;

    public PomodoroDetailService(PomodoroDetailRepository pomodoroDetailRepository) {
        this.pomodoroDetailRepository = pomodoroDetailRepository;
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

    @Transactional
    public void deletePomodoroDetail(Integer id) {
        pomodoroDetailRepository.deleteByPomodoroId(id);
    }
}

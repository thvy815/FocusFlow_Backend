package com.example.focusflow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.focusflow.entity.PomodoroDetail;
import com.example.focusflow.repository.PomodoroDetailRepository;

@Service
public class PomodoroDetailService {

    @Autowired
    private PomodoroDetailRepository repository;

    public PomodoroDetail create(PomodoroDetail detail) {
        return repository.save(detail);
    }
}

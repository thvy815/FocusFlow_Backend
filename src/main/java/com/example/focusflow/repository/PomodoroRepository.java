package com.example.focusflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.focusflow.entity.Pomodoro;

@Repository
public interface PomodoroRepository extends JpaRepository<Pomodoro, Integer> {
    List<Pomodoro> findByUserIdAndIsDeletedFalse(Integer userId);
}

package com.example.focusflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.focusflow.entity.PomodoroDetail;

@Repository
public interface PomodoroDetailRepository extends JpaRepository<PomodoroDetail, Long> {
    List<PomodoroDetail> findByPomodoroId(Long pomoId);
}

package com.example.focusflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.focusflow.entity.PomodoroDetail;

import jakarta.transaction.Transactional;

@Repository
public interface PomodoroDetailRepository extends JpaRepository<PomodoroDetail, Integer> {
    List<PomodoroDetail> findByPomodoroId(Integer pomodoroId);

    @Modifying
    @Transactional
    @Query("DELETE FROM PomodoroDetail pd WHERE pd.pomodoroId = :pomodoroId")
    void deleteByPomodoroId(@Param("pomodoroId") Integer pomodoroId);
}

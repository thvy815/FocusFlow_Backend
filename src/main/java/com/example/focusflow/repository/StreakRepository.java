package com.example.focusflow.repository;

import com.example.focusflow.entity.Streak;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StreakRepository extends JpaRepository<Streak, Integer> {
    Optional<Streak> findByUserId(Integer userId);
}
package com.example.focusflow.service;

import com.example.focusflow.entity.Streak;
import com.example.focusflow.repository.StreakRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Service
public class StreakService {
    private final StreakRepository streakRepository;

    public StreakService(StreakRepository streakRepository) {
        this.streakRepository = streakRepository;
    }

    public Streak getStreakByUserId(Integer userId) {
        return streakRepository.findByUserId(userId).orElse(null);
    }

    public void updateStreak(Integer userId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate today = LocalDate.now();

        Streak streak = streakRepository.findByUserId(userId).orElseGet(() -> {
            Streak s = new Streak();
            s.setUserId(userId);
            s.setCurrentStreak(0);
            s.setMaxStreak(0);
            s.setLastValidDate(null);
            return s;
        });

        String lastDateStr = streak.getLastValidDate();
        if (lastDateStr != null) {
            LocalDate lastDate = LocalDate.parse(lastDateStr, formatter);
            long daysBetween = ChronoUnit.DAYS.between(lastDate, today);

            if (daysBetween == 1) {
                streak.setCurrentStreak(streak.getCurrentStreak() + 1);
            } else if (daysBetween > 1) {
                streak.setCurrentStreak(1);
            } else {
                // Cùng ngày → đã tính rồi
                return;
            }
        } else {
            // lần đầu hoàn thành
            streak.setCurrentStreak(1);
        }

        streak.setLastValidDate(today.format(formatter));
        streak.setMaxStreak(Math.max(streak.getCurrentStreak(), streak.getMaxStreak()));
        streakRepository.save(streak);
    }

    public void resetStreak(Integer userId) {
        Streak streak = streakRepository.findByUserId(userId).orElseGet(() -> {
            Streak s = new Streak();
            s.setUserId(userId);
            return s;
        });

        streak.setCurrentStreak(0);
        streak.setMaxStreak(0);
        streak.setLastValidDate(null);
        streakRepository.save(streak);
    }
}

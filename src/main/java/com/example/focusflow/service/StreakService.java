package com.example.focusflow.service;

import com.example.focusflow.entity.Streak;
import com.example.focusflow.entity.Task;
import com.example.focusflow.repository.StreakRepository;
import com.example.focusflow.repository.TaskRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class StreakService {
    private final StreakRepository streakRepository;
    private final TaskRepository taskRepository;

    public StreakService(StreakRepository streakRepository, TaskRepository taskRepository) {
        this.streakRepository = streakRepository;
        this.taskRepository = taskRepository;
    }

    public Streak getStreakByUserId(Integer userId) {
        return streakRepository.findByUserId(userId).orElse(null);
    }

    public void updateStreak(Integer userId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate today = LocalDate.now();
        String todayStr = today.format(formatter);

        // Lấy hoặc tạo mới streak
        Streak streak = streakRepository.findByUserId(userId).orElseGet(() -> {
            Streak s = new Streak();
            s.setUserId(userId);
            s.setCurrentStreak(0);
            s.setMaxStreak(0);
            s.setLastValidDate(null);
            return s;
        });

        // Kiểm tra có task nào hoàn thành hôm nay không
        List<Task> tasksToday = taskRepository.findByUserIdAndDueDate(userId, todayStr);
        boolean hasCompletedToday = tasksToday.stream().anyMatch(task -> task.getIsCompleted());

        if (hasCompletedToday) {
            // Nếu chưa có trong validDates thì thêm vào
            if (!streak.getValidDates().contains(todayStr)) {
                streak.getValidDates().add(todayStr);
            }

            String lastDateStr = streak.getLastValidDate();
            if (lastDateStr != null) {
                LocalDate lastDate = LocalDate.parse(lastDateStr, formatter);
                long daysBetween = ChronoUnit.DAYS.between(lastDate, today);

                if (daysBetween == 1) {
                    streak.setCurrentStreak(streak.getCurrentStreak() + 1);
                } else if (daysBetween > 1) {
                    streak.setCurrentStreak(1); // reset lại vì bị đứt streak
                }
                // Nếu daysBetween == 0 (cùng ngày) thì không đổi streak
            } else {
                streak.setCurrentStreak(1); // lần đầu
            }

            // Cập nhật ngày cuối và max streak
            streak.setLastValidDate(todayStr);
            streak.setMaxStreak(Math.max(streak.getMaxStreak(), streak.getCurrentStreak()));

        } else {
            // Không có task nào hoàn thành hôm nay
            streak.getValidDates().remove(todayStr);

            // Nếu hôm nay là ngày cuối cùng hợp lệ thì xử lý
            if (todayStr.equals(streak.getLastValidDate())) {
                // Kiểm tra ngày hôm qua
                LocalDate yesterday = today.minusDays(1);
                String yesterdayStr = yesterday.format(formatter);

                if (streak.getValidDates().contains(yesterdayStr)) {
                    // Quay lại streak hôm qua
                    streak.setLastValidDate(yesterdayStr);
                    streak.setCurrentStreak(streak.getCurrentStreak() - 1);

                    // Nếu maxStreak đang bằng currentStreak + 1 → giảm luôn maxStreak
                    if (streak.getMaxStreak() == streak.getCurrentStreak() + 1) {
                        streak.setMaxStreak(streak.getCurrentStreak());
                    }

                } else {
                    // Không còn streak nào gần → reset
                    streak.setLastValidDate(null);
                    streak.setCurrentStreak(0);
                }
            }
        }

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
        if (streak.getValidDates() != null) {
            streak.getValidDates().clear();
        }
        streakRepository.save(streak);
    }
}

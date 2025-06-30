package com.example.focusflow.service;


import com.example.focusflow.entity.Streak;
import com.example.focusflow.entity.Task;
import com.example.focusflow.repository.StreakRepository;
import com.example.focusflow.repository.TaskRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class StreakService {
private final StreakRepository streakRepository;
    //private final TaskRepository taskRepository;
    private final TaskService taskService;

    public StreakService(StreakRepository streakRepository, TaskService taskService) {
        this.streakRepository = streakRepository;
        this.taskService = taskService;
    }

    public Streak updateStreakIfUserCompletedToday(Integer userId) {
    LocalDate today = LocalDate.now();

    // ✅ Dùng taskService để kiểm tra task đã hoàn thành hôm nay
    if (!taskService.hasCompletedAnyTaskToday(userId)) {
        return null;
    }

    Streak streak = streakRepository.findByUserId(userId).orElseGet(() -> {
        Streak s = new Streak();
        s.setUserId(userId);
        s.setCurrentStreak(0);
        s.setMaxStreak(0);
        return s;
    });

    if (streak.getLastValidDate() != null) {
        if (streak.getLastValidDate().plusDays(1).equals(today)) {
            streak.setCurrentStreak(streak.getCurrentStreak() + 1);
        } else if (!streak.getLastValidDate().equals(today)) {
            if (streak.getCurrentStreak() > streak.getMaxStreak()) {
                streak.setMaxStreak(streak.getCurrentStreak());
            }
            streak.setCurrentStreak(1);
        }
    } else {
        streak.setCurrentStreak(1);
    }

    streak.setLastValidDate(today);
    return streakRepository.save(streak);
}
public List<String> getCompletedStreakDates(Integer userId) {
    List<Task> tasks = taskService.getAllTasksRelatedToUser(userId);
    return tasks.stream()
        .filter(t -> Boolean.TRUE.equals(t.getIsCompleted()))
        .map(Task::getDueDate)
        .distinct()
        .toList();
}


}

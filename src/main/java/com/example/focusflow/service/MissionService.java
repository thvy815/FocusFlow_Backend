package com.example.focusflow.service;

import com.example.focusflow.entity.Pomodoro;
import com.example.focusflow.entity.Task;
import com.example.focusflow.entity.User;
import com.example.focusflow.repository.PomodoroRepository;
import com.example.focusflow.repository.TaskRepository;
import com.example.focusflow.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class MissionService {
    private final TaskRepository taskRepository;
    private final PomodoroRepository pomodoroRepository;
    private final UserRepository userRepository;

    public MissionService(TaskRepository taskRepository,
                          PomodoroRepository pomodoroRepository,
                          UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.pomodoroRepository = pomodoroRepository;
        this.userRepository = userRepository;
    }

    public int evaluateAndUpdateScore(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        int score = 0;
        String todayStr = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // Lấy tasks hoàn thành hôm nay
        List<Task> tasks = taskRepository.findByUserIdAndIsCompletedTrue(userId);
        long tasksToday = tasks.stream().filter(t -> todayStr.equals(t.getDueDate())).count();

        // Lấy pomodoro hôm nay
        List<Pomodoro> pomos = pomodoroRepository.findByUserIdAndIsDeletedFalse(userId);
        long pomosToday = pomos.stream().filter(p -> todayStr.equals(p.getDueDate())).count();

        // Tính điểm theo mission
        if (tasksToday >= 3) score += 1;
        if (pomosToday >= 3) score += 3;

        // Check nếu tất cả task hôm nay đã hoàn thành
        long allTodayTasks = taskRepository.findByUserIdAndDueDate(userId, todayStr).size();
        if (allTodayTasks > 0 && tasksToday == allTodayTasks) score += 3;

        // Cộng điểm vào user
        user.setScore(user.getScore() + score);
        userRepository.save(user);

        return score;
    }
}
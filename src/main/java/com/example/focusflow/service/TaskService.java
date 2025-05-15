package com.example.focusflow.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.focusflow.repository.TaskRepository;
import com.example.focusflow.entity.Task;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasksByUserId(Integer userId) {
        return taskRepository.findByUserIdAndIsDeletedFalse(userId);
    }

    public Optional<Task> getTaskById(Integer id) {
        return taskRepository.findById(id);
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTask(Integer id) {
        taskRepository.findById(id).ifPresent(task -> {
            task.setIsDeleted(true);
            taskRepository.save(task);
        });
    }
}

package com.example.focusflow.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.focusflow.entity.Task;
import com.example.focusflow.repository.CtGroupUserRepository;
import com.example.focusflow.repository.TaskRepository;;;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final CtGroupUserRepository ctGroupUserRepository;

    public TaskService(TaskRepository taskRepository,CtGroupUserRepository ctGroupUserRepository) {
        this.taskRepository = taskRepository;
        this.ctGroupUserRepository = ctGroupUserRepository;
    }

    public List<Task> getAllTasksByUserId(Integer userId) {
        return taskRepository.findByUserId(userId);
    }

    public Optional<Task> getTaskById(Integer id) {
        return taskRepository.findById(id);
    }

    // Tạo task theo ctId
    public Task createTaskForCt(Integer ctGroupId, Task task) {
        task.setCtGroupId(ctGroupId);
        return taskRepository.save(task);
    }
    // Lấy task theo groupId (truy ngược qua ctId)
    public List<Task> getTasksByGroupId(Integer groupId) {
        List<Integer> ctGroupId = ctGroupUserRepository.findCtGroupIdByGroupId(groupId);
        return taskRepository.findByCtGroupIdIn(ctGroupId);
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTask(Integer id) {
        taskRepository.deleteById(id);
    }
}

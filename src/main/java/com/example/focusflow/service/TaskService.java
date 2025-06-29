package com.example.focusflow.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.focusflow.entity.Task;
import com.example.focusflow.entity.TaskAssignment;
import com.example.focusflow.repository.CtGroupUserRepository;
import com.example.focusflow.repository.TaskAssignmentRepository;
import com.example.focusflow.repository.TaskRepository;;;

@Service
public class TaskService {
    
    private final TaskRepository taskRepository;

    private TaskAssignmentRepository taskAssignmentRepository;

    private final CtGroupUserRepository ctGroupUserRepository;

    public TaskService(TaskRepository taskRepository, CtGroupUserRepository ctGroupUserRepository,  TaskAssignmentRepository taskAssignmentRepository) {
        this.taskRepository = taskRepository;
        this.taskAssignmentRepository = taskAssignmentRepository;
        this.ctGroupUserRepository = ctGroupUserRepository;
    }
    
    // Lấy task cá nhân (userId tạo) + task nhóm được phân công
    public List<Task> getAllTasksRelatedToUser(Integer userId) {
        List<Task> personalTasks = taskRepository.findPersonalTasksByUserId(userId);
        List<Task> groupTasks = taskRepository.findGroupTasksByUserId(userId);

        personalTasks.addAll(groupTasks);
        return personalTasks;
    }

    public Optional<Task> getTaskById(Integer id) {
        return taskRepository.findById(id);
    }

    // Lấy task theo groupId → truy ngược ct_id → tìm trong task_assignment
    public List<Task> getTasksByGroupId(Integer groupId) {
        List<Integer> ctGroupIds = ctGroupUserRepository.findCtGroupIdByGroupId(groupId);
        return taskRepository.findByCtGroupIdIn(ctGroupIds);
    }

    // Tạo task cá nhân (không có ctGroupIds)
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    // Tạo task + gán danh sách ctGroupId (phân công nhiều người)
    public Task createTask(Task task, List<Integer> ctGroupIds) {
        Task savedTask = taskRepository.save(task);

        if (ctGroupIds != null && !ctGroupIds.isEmpty()) {
            for (Integer ctGroupId : ctGroupIds) {
                TaskAssignment assignment = new TaskAssignment(savedTask.getId(), ctGroupId);
                taskAssignmentRepository.save(assignment);
            }
        }

        return savedTask;
    }

    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }

    public void deleteTask(Integer id) {
        taskRepository.deleteById(id);
    }
}

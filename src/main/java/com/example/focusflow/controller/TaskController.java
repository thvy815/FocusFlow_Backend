package com.example.focusflow.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.focusflow.entity.Task;
import com.example.focusflow.entity.User;
import com.example.focusflow.model.TaskGroupRequest;
import com.example.focusflow.service.TaskService;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // GET task cá nhân + nhóm liên quan
    @GetMapping("/user/{userId}")
    public List<Task> getTasksByUser(@PathVariable Integer userId) {
        return taskService.getAllTasksRelatedToUser(userId);
    }

    @GetMapping("/{id}")
    public Optional<Task> getTaskById(@PathVariable Integer id) {
        return taskService.getTaskById(id);
    }

    // Tạo task cá nhân hoặc nhóm tùy theo có ctGroupIds hay không
    @PostMapping
    public Task createTask(@RequestBody TaskGroupRequest dto) {
        Task task = new Task(
                null,
                dto.userId,
                dto.title,
                dto.description,
                dto.dueDate,
                dto.time,
                dto.tag,
                dto.priority,
                dto.repeatStyle,
                dto.reminderStyle
        );

        // nếu không có danh sách ctGroupIds → tạo task cá nhân
        if (dto.ctGroupIds == null || dto.ctGroupIds.isEmpty()) {
            return taskService.createTask(task);
        } else {
            return taskService.createTask(task, dto.ctGroupIds); // tạo task nhóm + gán assignment
        }
    }

    @GetMapping("/{taskId}/assignees")
    public ResponseEntity<List<User>> getAssignees(@PathVariable int taskId) {
        List<User> users = taskService.getAssigneesOfTask(taskId);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Integer id, @RequestBody Task task) {
        task.setId(id);
        return taskService.updateTask(task);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Integer id) {
        taskService.deleteTask(id);
    }

    @GetMapping("/group/{groupId}")
    public List<Task> getTasksByGroup(@PathVariable Integer groupId) {
        return taskService.getTasksByGroupId(groupId);
    }
}

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
import com.example.focusflow.service.StreakService;
import com.example.focusflow.service.TaskService;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final StreakService streakService;

    public TaskController(TaskService taskService, StreakService streakService) {
        this.taskService = taskService;
        this.streakService = streakService;
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
        task.setIsCompleted(dto.isCompleted != null && dto.isCompleted);

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

    @PutMapping
    public Task updateTask(@RequestBody TaskGroupRequest dto) {
        // Lấy task cũ từ DB
        Optional<Task> optionalOldTask = taskService.getTaskById(dto.taskId);
        boolean isCompletedChanged = false;
        Boolean oldCompleted = null;

        // So sánh trạng thái hoàn thành cũ
        if (optionalOldTask.isPresent()) {
            oldCompleted = optionalOldTask.get().getIsCompleted();
        }

        Task task = new Task(
            dto.taskId,
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
        task.setIsCompleted(dto.isCompleted != null && dto.isCompleted);

        // So sánh nếu oldCompleted có giá trị
        if (oldCompleted != null && oldCompleted != task.getIsCompleted()) {
            isCompletedChanged = true;
        }

        // Cập nhật task
        Task updatedTask;
        if (dto.ctGroupIds == null || dto.ctGroupIds.isEmpty()) {
            updatedTask = taskService.updateTask(task); // chỉ update task
        } else {
            updatedTask = taskService.updateTask(task, dto.ctGroupIds); // update task + phân công
        }

        // Chỉ cập nhật streak nếu trạng thái hoàn thành thay đổi
        if (isCompletedChanged) {
            streakService.updateStreak(dto.userId);
        }

        return updatedTask;
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

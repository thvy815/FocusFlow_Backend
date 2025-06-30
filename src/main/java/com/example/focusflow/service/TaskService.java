package com.example.focusflow.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.focusflow.entity.CtGroupUser;
import com.example.focusflow.entity.Task;
import com.example.focusflow.entity.TaskAssignment;
import com.example.focusflow.entity.User;
import com.example.focusflow.model.TaskGroupMessage;
import com.example.focusflow.repository.CtGroupUserRepository;
import com.example.focusflow.repository.TaskAssignmentRepository;
import com.example.focusflow.repository.TaskRepository;
import com.example.focusflow.repository.UserRepository;

import jakarta.transaction.Transactional;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Service
public class TaskService {
    
    private final TaskRepository taskRepository;
    private TaskAssignmentRepository taskAssignmentRepository;
    private final CtGroupUserRepository ctGroupUserRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public TaskService(TaskRepository taskRepository, 
                       CtGroupUserRepository ctGroupUserRepository,  
                       TaskAssignmentRepository taskAssignmentRepository,
                       UserRepository userRepository,
                       SimpMessagingTemplate messagingTemplate) {
        this.taskRepository = taskRepository;
        this.taskAssignmentRepository = taskAssignmentRepository;
        this.ctGroupUserRepository = ctGroupUserRepository;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
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

    // Tạo task nhóm + gán danh sách ctGroupId (phân công nhiều người)
    public Task createTask(Task task, List<Integer> ctGroupIds) {
        Task savedTask = taskRepository.save(task);

        if (ctGroupIds != null && !ctGroupIds.isEmpty()) {
            for (Integer ctGroupId : ctGroupIds) {
                taskAssignmentRepository.save(new TaskAssignment(savedTask.getId(), ctGroupId));

                // 🔔 Gửi notify tới group
                Optional<CtGroupUser> ct = ctGroupUserRepository.findById(ctGroupId);
                ct.ifPresent(ctGroupUser -> {
                    Integer groupId = ctGroupUser.getGroupId();
                    
                    // 🔔 Gửi TaskMessage dạng "created"
                    TaskGroupMessage message = new TaskGroupMessage("created", savedTask);
                    messagingTemplate.convertAndSend("/topic/group/" + groupId, message);
                });
            }
        }

        return savedTask;
    }

    public List<User> getAssigneesOfTask(int taskId) {
        List<TaskAssignment> assignments = taskAssignmentRepository.findByTaskId(taskId);
        List<Integer> userIds = new ArrayList<>();

        for (TaskAssignment assignment : assignments) {
            CtGroupUser ct = ctGroupUserRepository.findById(assignment.getCtGroupId()).orElse(null);
            if (ct != null) {
                userIds.add(ct.getUserId());
            }
        }

        return userRepository.findAllById(userIds);
    }

    //Hàm kiểm tra cho Streak
    public boolean hasCompletedAnyTaskToday(Integer userId) {
    List<Task> allTasks = getAllTasksRelatedToUser(userId);
    String todayStr = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

    return allTasks.stream()
        .anyMatch(task ->
            Boolean.TRUE.equals(task.getIsCompleted()) &&
            todayStr.equals(task.getDueDate())
        );
    }


    // Cập nhật task cá nhân (không có ctGroupIds)
    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }

    @Transactional
    // Cập nhật task nhóm + gán danh sách ctGroupId (phân công nhiều người)
    public Task updateTask(Task task, List<Integer> ctGroupIds) {
        // Cập nhật thông tin task cơ bản
        Task updatedTask = taskRepository.save(task);

        if (ctGroupIds != null && !ctGroupIds.isEmpty()) {
            // Xóa tất cả phân công cũ của task này
            taskAssignmentRepository.deleteByTaskId(task.getId());
            
            // Thêm lại phân công mới
            for (Integer ctGroupId : ctGroupIds) {
                TaskAssignment assignment = new TaskAssignment(task.getId(), ctGroupId);
                taskAssignmentRepository.save(assignment);

                Optional<CtGroupUser> ct = ctGroupUserRepository.findById(ctGroupId);
                ct.ifPresent(ctGroupUser -> {
                    Integer groupId = ctGroupUser.getGroupId();

                    // 🔔 Gửi TaskMessage dạng "updated"
                    TaskGroupMessage message = new TaskGroupMessage("updated", updatedTask);
                    messagingTemplate.convertAndSend("/topic/group/" + groupId, message);
                });
            }
        }  
        return updatedTask;
    }

    public void deleteTask(Integer id) {
        Optional<Task> taskOpt = taskRepository.findById(id);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();

            // Lấy groupId thông qua assignment
            List<TaskAssignment> assignments = taskAssignmentRepository.findByTaskId(id);
            for (TaskAssignment assignment : assignments) {
                Optional<CtGroupUser> ct = ctGroupUserRepository.findById(assignment.getCtGroupId());
                ct.ifPresent(ctGroupUser -> {
                    Integer groupId = ctGroupUser.getGroupId();

                    // 🔔 Gửi TaskMessage dạng "deleted"
                    TaskGroupMessage message = new TaskGroupMessage("deleted", task);
                    messagingTemplate.convertAndSend("/topic/group/" + groupId, message);
                });
            }
            // Xóa tất cả các assignment liên quan trước
            taskAssignmentRepository.deleteByTaskId(id);
            
            // Xóa task
            taskRepository.deleteById(id);
        }
    }
}
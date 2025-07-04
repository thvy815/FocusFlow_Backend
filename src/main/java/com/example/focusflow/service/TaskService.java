package com.example.focusflow.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.focusflow.entity.CtGroupUser;
import com.example.focusflow.entity.Task;
import com.example.focusflow.entity.TaskAssignment;
import com.example.focusflow.entity.User;
import com.example.focusflow.model.TaskDTO;
import com.example.focusflow.model.TaskGroupMessage;
import com.example.focusflow.model.UserDTO;
import com.example.focusflow.repository.CtGroupUserRepository;
import com.example.focusflow.repository.TaskAssignmentRepository;
import com.example.focusflow.repository.TaskRepository;
import com.example.focusflow.repository.UserRepository;

import jakarta.transaction.Transactional;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Service
public class TaskService {
    
    private final TaskRepository taskRepository;
    private final TaskAssignmentRepository taskAssignmentRepository;
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
        Integer groupId = null;

        if (ctGroupIds != null && !ctGroupIds.isEmpty()) {
            // Lưu assignment mới
            for (Integer ctGroupId : ctGroupIds) {
                taskAssignmentRepository.save(new TaskAssignment(savedTask.getId(), ctGroupId));

                // Lấy groupId từ ctGroupUser
                Optional<CtGroupUser> ctOpt = ctGroupUserRepository.findById(ctGroupId);
                if (ctOpt.isPresent() && groupId == null) {
                    groupId = ctOpt.get().getGroupId(); // Chỉ cần lấy 1 lần
                }
            }

            if (groupId != null) {
                // 🔁 1. Gửi đến toàn bộ thành viên theo userId để hiện notification
                List<CtGroupUser> groupUsers = ctGroupUserRepository.findByGroupId(groupId);
                for (CtGroupUser ctUser : groupUsers) {
                    TaskGroupMessage message = new TaskGroupMessage("created", savedTask);
                    messagingTemplate.convertAndSend("/topic/user/" + ctUser.getUserId(), message);
                }

                // 🔁 2. Gửi đến nhóm để cập nhật UI task list
                TaskGroupMessage groupMessage = new TaskGroupMessage("created", savedTask);
                messagingTemplate.convertAndSend("/topic/group/" + groupId, groupMessage);
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

    public List<TaskDTO> getTasksWithAssignedUsersByGroupId(Integer groupId) {
        // Lấy tất cả task có liên kết gián tiếp với group thông qua task_assignment & ct_group_user
        List<Task> tasks = taskRepository.findTasksByGroupId(groupId);

        // Với mỗi task, lấy danh sách User được phân công thông qua bảng trung gian
        return tasks.stream().map(task -> {
            List<User> users = taskAssignmentRepository.findUsersAssignedToTask(task.getId());
            List<UserDTO> userDTOs = users.stream().map(UserDTO::new).collect(Collectors.toList());
            return new TaskDTO(task, userDTOs);
        }).collect(Collectors.toList());
    }

    // Cập nhật task cá nhân (không có ctGroupIds)
    public Task updateTask(Task task) {
        Task updatedTask = taskRepository.save(task);

        // Gửi WebSocket nếu là task nhóm
        List<TaskAssignment> assignments = taskAssignmentRepository.findByTaskId(task.getId());
        if (!assignments.isEmpty()) {
            Integer ctGroupId = assignments.get(0).getCtGroupId();
            Optional<CtGroupUser> ct = ctGroupUserRepository.findById(ctGroupId);
            ct.ifPresent(ctGroupUser -> {
                Integer groupId = ctGroupUser.getGroupId();
                TaskGroupMessage message = new TaskGroupMessage("updated", updatedTask);
                messagingTemplate.convertAndSend("/topic/group/" + groupId, message);
            });
        }

        return updatedTask;
    }

    @Transactional
    // Cập nhật task nhóm + gán danh sách ctGroupId (phân công nhiều người)
    public Task updateTask(Task task, List<Integer> ctGroupIds) {
        // Cập nhật thông tin task cơ bản
        Task updatedTask = taskRepository.save(task);

        if (ctGroupIds == null || ctGroupIds.isEmpty()) {
            // ✅ Chỉ gửi thông báo "updated" cho group chứa task

            // Lấy danh sách phân công cũ → từ đó tìm groupId
            List<TaskAssignment> oldAssignments = taskAssignmentRepository.findByTaskId(task.getId());
            if (!oldAssignments.isEmpty()) {
                // Lấy ctGroupId đầu tiên để truy ra groupId
                Integer ctGroupId = oldAssignments.get(0).getCtGroupId();
                Optional<CtGroupUser> ct = ctGroupUserRepository.findById(ctGroupId);
                ct.ifPresent(ctGroupUser -> {
                    Integer groupId = ctGroupUser.getGroupId();
                    TaskGroupMessage message = new TaskGroupMessage("updated", updatedTask);
                    messagingTemplate.convertAndSend("/topic/group/" + groupId, message);
                });
            }

            return updatedTask;
        }

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

    @Transactional
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
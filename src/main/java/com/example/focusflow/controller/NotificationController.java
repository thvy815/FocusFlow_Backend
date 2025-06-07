package com.example.focusflow.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.focusflow.model.TaskNotificationScheduler;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final TaskNotificationScheduler scheduler;

    public NotificationController(TaskNotificationScheduler scheduler) {
        this.scheduler = scheduler;
    }

    @PostMapping("/send")
    public String sendTestNotification() {
        scheduler.checkTasksAndSendNotifications();
        return "Notification check triggered";
    
    }

    
}

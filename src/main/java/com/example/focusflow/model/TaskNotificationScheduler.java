package com.example.focusflow.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.focusflow.entity.Task;
import com.example.focusflow.repository.TaskRepository;
import com.example.focusflow.repository.UserRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

@Component
public class TaskNotificationScheduler {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Scheduled(fixedRate = 60000) // chạy mỗi phút (60,000 ms)
    public void checkTasksAndSendNotifications() {
        List<Task> tasks = taskRepository.findAll();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        for (Task task : tasks) {
            if (Boolean.TRUE.equals(task.getIsCompleted())) continue;

            try {
                String dateStr = task.getDueDate();
                if (dateStr == null || dateStr.isBlank()) {
                    continue; // hoặc xử lý mặc định
                }
                LocalDate taskDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                String timeStr = task.getTime();
                if (timeStr == null || timeStr.isBlank()) {
                    continue; // hoặc bỏ qua task này nếu không có time
                }
                LocalTime taskTime = LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm"));


                // Gửi thông báo nếu task tới hạn vào ngày mai
                if (taskDate.equals(today.plusDays(1))) {
                    sendPushNotification(task, "Task due tomorrow: " + task.getTitle());
                }

                // Gửi thông báo nếu task hôm nay và sắp đến trong vòng 1 tiếng
                if (taskDate.equals(today)) {
                    if (taskTime.minusHours(1).isBefore(now) && taskTime.isAfter(now)) {
                        sendPushNotification(task, "Task in 1 hour: " + task.getTitle());
                    }
                }

            } catch (DateTimeParseException e) {
                System.err.println("❌ Invalid date/time format for task ID " + task.getId());
            }
        }
    }

    private void sendPushNotification(Task task, String messageBody) {
        // lấy token của user qua userId
        String deviceToken = getTokenFromUserId(task.getUserId()); 

        if (deviceToken == null) return;

        Message message = Message.builder()
                .setToken(deviceToken)
                .setNotification(Notification.builder()
                        .setTitle("FocusFlow Reminder")
                        .setBody(messageBody)
                        .build())
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("✅ Notification sent: " + response);

            System.out.println("📬 Đã gửi notification cho task: " + task.getTitle() + " của user ID: " + task.getUserId());
        } catch (Exception e) {
            System.err.println("❌ Failed to send notification: " + e.getMessage());
        }
    }

    private String getTokenFromUserId(Integer userId) {
        return userRepository.findById(userId)
            .map(user -> user.getFcmToken())
            .orElse(null);
    }

    
}

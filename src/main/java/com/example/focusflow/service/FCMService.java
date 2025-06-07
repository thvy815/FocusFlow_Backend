package com.example.focusflow.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class FCMService {

    public String sendMessage(String token, String title, String body) {
        try {
            Message message = Message.builder()
                .setToken(token)
                .setNotification(Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build())
                .build();

            return FirebaseMessaging.getInstance().send(message);

        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Error sending FCM: " + e.getMessage();
        }
    }
}

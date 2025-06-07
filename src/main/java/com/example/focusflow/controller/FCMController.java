package com.example.focusflow.controller;

import com.example.focusflow.service.FCMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/fcm")
public class FCMController {

    @Autowired
    private FCMService fcmService;

    @PostMapping("/send")
    public String sendNotification(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        String title = payload.get("title");
        String body = payload.get("body");

        return fcmService.sendMessage(token, title, body);
    }
}

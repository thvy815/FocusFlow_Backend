package com.example.focusflow.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.focusflow.service.OpenAIService;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin
public class AIController {

    private final OpenAIService openAIService;

    public AIController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping("/chat")
    public ResponseEntity<String> chatWithAI(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");
        String aiReply = openAIService.sendMessage(userMessage);
        return ResponseEntity.ok(aiReply);
    }

    @GetMapping("/chat")
    public ResponseEntity<String> chatWithAIGet(@RequestParam(defaultValue = "Xin chào") String message) {
        String aiReply = openAIService.sendMessage(message);
        return ResponseEntity.ok(aiReply);
    }
}

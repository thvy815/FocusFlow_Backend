package com.example.focusflow.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OpenAIService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    public String sendMessage(String message) {
        RestTemplate restTemplate = new RestTemplate();

        // 1. Tạo headers
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey); // "Bearer sk-xxxx"
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("HTTP-Referer", "http://localhost"); // Bắt buộc với OpenRouter
        headers.set("X-Title", "FocusFlow");          // Tên app bạn chọn

        // 2. Tạo body gửi đi
        Map<String, Object> body = new HashMap<>();
        body.put("model", "openai/gpt-3.5-turbo"); // Model cho OpenRouter
        body.put("messages", List.of(Map.of(
                "role", "user",
                "content", message
        )));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            // 3. Gửi request
            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            // 4. Đọc kết quả
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            return root.path("choices").get(0).path("message").path("content").asText();

        } catch (Exception e) {
            return "Lỗi khi gọi AI: " + e.getMessage();
        }
    }
}
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

        // 1. Headers
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey); // "Bearer ..."
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("HTTP-Referer", "http://localhost"); // Required for OpenRouter
        headers.set("X-Title", "FocusFlow");

        // 2. Request body
        Map<String, Object> body = new HashMap<>();
        body.put("model", "deepseek/deepseek-r1-0528:free"); // updated model
        body.put("messages", List.of(
            Map.of("role", "user", "content", message)
        ));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            return root.path("choices").get(0).path("message").path("content").asText();

        } catch (Exception e) {
            return "Lỗi khi gọi AI: " + e.getMessage();
        }
    }
}
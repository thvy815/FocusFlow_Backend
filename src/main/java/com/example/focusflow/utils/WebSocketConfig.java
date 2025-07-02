package com.example.focusflow.utils;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // client sẽ subscribe vào đây
        config.setApplicationDestinationPrefixes("/app"); // prefix khi gửi từ client
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // ✅ Dùng cho Android (native WebSocket)
        registry.addEndpoint("/ws/websocket")
                .setAllowedOriginPatterns("*"); // Cho phép mọi nguồn (có thể thay bằng domain/IP cụ thể nếu cần)

        // ✅ Dùng cho frontend web nếu cần SockJS
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}


package com.hrms.backend.configs;

import com.hrms.backend.websocket.CustomHandshakeHandler;
import com.hrms.backend.websocket.UserHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // Enables WebSocket message handling backed by a message broker (i.e., STOMP).
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat-websocket")
                .setHandshakeHandler(new CustomHandshakeHandler())
                .addInterceptors(new UserHandshakeInterceptor())
                .setAllowedOriginPatterns("*") // Allows cross-origin requests (for frontend clients)
                .withSockJS();                // Enables SockJS fallback (for clients that don't support native WebSocket)

        registry.addEndpoint("/webrtc")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic","/queue"); // Enable in-memory message broker for topics // // can give more than one topic in this only
        config.setApplicationDestinationPrefixes("/msg"); // Prefix for messages sent from client to server // can give more than one prefix in this only
        config.setUserDestinationPrefix("/user"); // 1 to 1 chat
    }
}


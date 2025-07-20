package com.hrms.backend.configs;

import com.hrms.backend.controllers.ChatWebSocketController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker // Enables WebSocket message handling backed by a message broker (i.e., STOMP).
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private UserHandshakeInterceptor handshakeInterceptor;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat-websocket")
                .setAllowedOriginPatterns("*") // Allows cross-origin requests (for frontend clients)
                .addInterceptors(handshakeInterceptor)
                .withSockJS();                // Enables SockJS fallback (for clients that don't support native WebSocket)

        registry.addEndpoint("/webrtc")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // Enable in-memory message broker for topics // // can give more than one topic in this only
        config.setApplicationDestinationPrefixes("/msg"); // Prefix for messages sent from client to server // can give more than one prefix in this only
        config.setUserDestinationPrefix("/user"); // 1 to 1 chat
    }

    @Bean
    public DefaultHandshakeHandler handshakeHandler() {
        return new DefaultHandshakeHandler() {
            @Override
            protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
                String userId = (String) attributes.get("userId");
                return () -> userId; // 👈 This will be the user's "Principal"
            }
        };
    }

}

@Component
class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(ChatWebSocketController.class);

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        logger.info("🟢 WebSocket connected: sessionId = {}", accessor.getUser());
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        logger.info("🔴 WebSocket disconnected: sessionId = {}", accessor.getSessionId());
    }
}
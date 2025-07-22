package com.hrms.backend.websocket;

import com.hrms.backend.controllers.ChatWebSocketController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(ChatWebSocketController.class);

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        logger.info("🟢 WebSocket connected: sessionId = {}", accessor.getSessionId());
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        logger.info("🔴 WebSocket disconnected: sessionId = {}", accessor.getSessionId());
    }

    @EventListener
    public void onApplicationEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        String sessionId = accessor.getSessionId();
        String destination = accessor.getDestination();
        String user = accessor.getUser() != null ? accessor.getUser().getName() : "anonymous";

        logger.info("User subscribed: sessionId={}, user={}, destination={}", sessionId, user, destination);
    }
}

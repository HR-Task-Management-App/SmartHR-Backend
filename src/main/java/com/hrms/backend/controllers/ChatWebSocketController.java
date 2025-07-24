package com.hrms.backend.controllers;

import com.hrms.backend.dtos.entityDtos.ChatMessage.ChatMessageRequestDto;
import com.hrms.backend.dtos.entityDtos.ChatMessage.SeenMessagePayload;
import com.hrms.backend.services.websocketService.ChatWebSocketService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {

    @Autowired
    private ChatWebSocketService chatWebSocketService;

    private static final Logger logger = LoggerFactory.getLogger(ChatWebSocketController.class);


    @MessageMapping("/chat/send") // /msg/chat/send
    public void sendMessage(@Valid @Payload ChatMessageRequestDto messageDto)  {
        logger.info("📨 Message from {} to {}: {}", messageDto.getSender(), messageDto.getReceiver(), messageDto.getContent());
        chatWebSocketService.processMessage(messageDto);
    }

    @MessageMapping("/chat/seen")
    private void seenMessage(@Payload SeenMessagePayload payload){
        logger.info("📨 Seen Message {} {}",payload.getChatId(),payload.getUserId());
        chatWebSocketService.handleSeenMessage(payload);
    }
}

package com.hrms.backend.controllers;

import com.hrms.backend.dtos.entityDtos.Chat.ChatMessageDto;
import com.hrms.backend.services.chatService.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {

    @Autowired
    private ChatService chatService;

    private static final Logger logger = LoggerFactory.getLogger(ChatWebSocketController.class);


    @MessageMapping("/chat/send") // /msg/chat/send
    public void sendMessage(ChatMessageDto messageDto) {
        logger.info("📨 Message from {} to {}: {}", messageDto.getSenderId(), messageDto.getReceiverId(), messageDto.getContent());
        chatService.processMessage(messageDto);
    }
}

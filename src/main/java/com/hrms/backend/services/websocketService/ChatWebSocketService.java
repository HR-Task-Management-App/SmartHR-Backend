package com.hrms.backend.services.websocketService;

import com.hrms.backend.dtos.entityDtos.ChatMessage.ChatMessageRequestDto;
import com.hrms.backend.dtos.entityDtos.ChatMessage.ChatMessageResponseDto;
import com.hrms.backend.services.chatService.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ChatWebSocketService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatService chatService;

    @Autowired
    private ModelMapper mapper;

    public void processMessage(ChatMessageRequestDto chatMessageRequestDto) {
        ChatMessageResponseDto chatMessageResponseDto = chatService.saveMessage(chatMessageRequestDto);

        String senderId = chatMessageResponseDto.getSender().getId();
        String receiverId = chatMessageResponseDto.getReceiver().getId();

        // Send to receiver
        messagingTemplate.convertAndSendToUser(
                receiverId,
                "/queue/messages",
                chatMessageResponseDto
        );

        // Send to sender (back to themselves)
        messagingTemplate.convertAndSendToUser(
                senderId,
                "/queue/messages",
                chatMessageResponseDto
        );
    }
}

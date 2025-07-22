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

        messagingTemplate.convertAndSendToUser( //for private chatting 1 to 1
                chatMessageResponseDto.getReceiver().getId(),   // Spring will internally send to /user/{receiverId}/queue/messages
                "/queue/messages",
                chatMessageResponseDto
        );
    }
}

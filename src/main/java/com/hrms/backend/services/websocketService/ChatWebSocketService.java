package com.hrms.backend.services.websocketService;

import com.hrms.backend.dtos.entityDtos.ChatMessage.ChatMessageRequestDto;
import com.hrms.backend.dtos.entityDtos.ChatMessage.ChatMessageResponseDto;
import com.hrms.backend.dtos.entityDtos.ChatMessage.SeenMessagePayload;
import com.hrms.backend.models.Chat;
import com.hrms.backend.repositories.ChatRepository;
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
    private ChatRepository chatRepository;

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

    public void handleSeenMessage(SeenMessagePayload payload) {
        String chatId = payload.getChatId();
        String receiverId = payload.getUserId();

        chatService.markMessagesAsSeen(chatId, receiverId);

        // Notify sender
        Chat chat = chatRepository.findById(chatId).orElseThrow();
        String senderId = chat.getUser1().equals(receiverId) ? chat.getUser2() : chat.getUser1();

        messagingTemplate.convertAndSendToUser(
                senderId,
                "/queue/message-seen",
                new SeenMessagePayload(chatId,receiverId)
        );
    }

}

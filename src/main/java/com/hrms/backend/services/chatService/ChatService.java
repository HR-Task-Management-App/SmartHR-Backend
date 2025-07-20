package com.hrms.backend.services.chatService;

import com.hrms.backend.dtos.entityDtos.Chat.ChatMessageDto;
import com.hrms.backend.models.ChatMessage;
import com.hrms.backend.repositories.ChatMessageRepository;
import com.hrms.backend.utils.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ChatService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatMessageRepository messageRepository;

    public void processMessage(ChatMessageDto dto) {
        String encrypted;
        try {
            encrypted = EncryptionUtil.encrypt(dto.getContent());
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed");
        }

        ChatMessage msg = new ChatMessage();
        msg.setSenderId(dto.getSenderId());
        msg.setReceiverId(dto.getReceiverId());
        msg.setType(dto.getType());
        msg.setCompanyCode(dto.getCompanyCode());
        msg.setEncryptedContent(encrypted);
        msg.setTimestamp(LocalDateTime.now());
        msg.setDelivered(true);

        messageRepository.save(msg);

        messagingTemplate.convertAndSendToUser( //for private chatting 1 to 1
                dto.getReceiverId(),   // Spring will internally send to /user/{receiverId}/queue/messages
                "/queue/messages",
                dto
        );
    }
}

package com.hrms.backend.controllers;

import com.hrms.backend.dtos.entityDtos.Chat.ChatMessageDto;
import com.hrms.backend.models.ChatMessage;
import com.hrms.backend.repositories.ChatMessageRepository;
import com.hrms.backend.utils.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chats")
public class ChatHistoryController {

    @Autowired
    private ChatMessageRepository repository;

    @GetMapping("/history")
    public List<ChatMessageDto> getMessages(
            @RequestParam String userId,
            @RequestParam String otherUserId,
            @RequestParam String companyCode
    ) throws Exception {
        List<ChatMessage> messages = repository.findAllByCompanyCodeAndSenderIdAndReceiverIdOrderByTimestampAsc(
                companyCode, userId, otherUserId
        );

        return messages.stream()
                .map(msg -> {
                    try {
                        return new ChatMessageDto(
                            msg.getSenderId(),
                            msg.getReceiverId(),
                            EncryptionUtil.decrypt(msg.getEncryptedContent()),
                            msg.getType(),
                            msg.getCompanyCode(),
                            msg.getTimestamp()
                        );
                    } catch (Exception e) {
                        throw new RuntimeException("Decryption failed");
                    }
                })
                .toList();
    }
}

package com.hrms.backend.dtos.entityDtos.ChatMessage;

import com.hrms.backend.dtos.entityDtos.User.UserInfo;
import com.hrms.backend.models.User;
import com.hrms.backend.models.enums.MessageStatus;
import com.hrms.backend.models.enums.MessageType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageResponseDto {
    private String id;
    private String chatId;
    private UserInfo sender;
    private UserInfo receiver;
    private String content; // Encrypted message or file link
    private String messageType; // TEXT, IMAGE, VIDEO
    private String companyCode;
    private String timestamp;
    private String messageStatus;
}

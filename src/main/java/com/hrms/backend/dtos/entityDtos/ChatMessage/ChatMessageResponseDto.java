package com.hrms.backend.dtos.entityDtos.ChatMessage;

import com.hrms.backend.dtos.entityDtos.User.UserInfo;
import lombok.*;

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

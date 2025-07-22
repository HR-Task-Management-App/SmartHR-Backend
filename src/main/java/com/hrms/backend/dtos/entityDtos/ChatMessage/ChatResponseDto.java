package com.hrms.backend.dtos.entityDtos.ChatMessage;

import com.hrms.backend.dtos.entityDtos.User.UserInfo;
import com.hrms.backend.models.enums.MessageStatus;
import com.hrms.backend.models.enums.MessageType;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatResponseDto {

    private String id;
    private String chatId;

    private UserInfo user1;
    private UserInfo user2;

    private String lastMessage;
    private String lastUpdated;

    private String companyCode;

    private String lastMessageType;
    private String lastMessageStatus;
}

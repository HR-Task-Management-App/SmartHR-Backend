package com.hrms.backend.dtos.entityDtos.ChatMessage;

import com.hrms.backend.dtos.entityDtos.User.UserInfo;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatResponseDto {

    private String id;

    private UserInfo user1;
    private UserInfo user2;

    private String lastMessage;
    private String lastUpdated;

    private String companyCode;

    private String lastMessageType;
    private String lastMessageStatus;
    private String lastMessageSender;
}

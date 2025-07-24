package com.hrms.backend.models;

import com.hrms.backend.models.enums.MessageStatus;
import com.hrms.backend.models.enums.MessageType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "chats")
public class Chat {

    @Id
    private String id;

    private String user1;
    private String user2;

    private String lastMessage;
    private LocalDateTime lastUpdated;

    private String companyCode;

    private MessageType lastMessageType;
    private MessageStatus lastMessageStatus;
    private String lastMessageSender; // senderId
    private String lastSeenMessageId;
}

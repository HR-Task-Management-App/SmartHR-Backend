package com.hrms.backend.models;

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
@Document(collection = "chat_messages")
public class ChatMessage {
    @Id
    private String id;

    private String senderId;
    private String receiverId;

    private String encryptedContent; // Encrypted message or file link
    private MessageType type; // TEXT, IMAGE, VIDEO

    private String companyCode;

    private LocalDateTime timestamp;

    private boolean delivered;
    private boolean seen;
}

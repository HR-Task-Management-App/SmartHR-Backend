package com.hrms.backend.dtos.entityDtos.Chat;

import com.hrms.backend.models.enums.MessageType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDto {

    private String senderId;
    private String receiverId;

    private String content; // plain text or file link
    private MessageType type; // TEXT, IMAGE, VIDEO

    private String companyCode;

    private LocalDateTime timestamp;

}

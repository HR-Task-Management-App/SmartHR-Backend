package com.hrms.backend.dtos.entityDtos.ChatMessage;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeenMessagePayload {
    private String chatId;
    private String userId;
}
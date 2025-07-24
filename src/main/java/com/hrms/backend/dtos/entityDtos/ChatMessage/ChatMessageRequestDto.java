package com.hrms.backend.dtos.entityDtos.ChatMessage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageRequestDto {
    @NotNull
    @NotBlank
    private String sender;
    @NotNull
    @NotBlank
    private String receiver;
    @NotNull
    @NotBlank
    private String content; // plain text or file link
    @NotBlank
    @NotNull
    @Pattern(regexp = "TEXT|IMAGE|VIDEO", message = "Message type must be TEXT, IMAGE, VIDEO")
    private String messageType; // TEXT, IMAGE, VIDEO
    @NotNull
    @NotBlank
    private String companyCode;
}

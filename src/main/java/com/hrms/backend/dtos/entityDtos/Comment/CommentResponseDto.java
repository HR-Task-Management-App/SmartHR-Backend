package com.hrms.backend.dtos.entityDtos.Comment;

import com.hrms.backend.dtos.entityDtos.User.UserInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Builder
@Getter
@Setter
public class CommentResponseDto {
    private String id;
    private String taskId;
    private String text;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserInfo author; // fetched via userId
}

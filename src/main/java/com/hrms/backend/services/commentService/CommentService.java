package com.hrms.backend.services.commentService;

import com.hrms.backend.dtos.entityDtos.Comment.CommentRequestDto;
import com.hrms.backend.dtos.entityDtos.Comment.CommentResponseDto;

import java.util.List;

public interface CommentService {
    CommentResponseDto addComment(String userId, CommentRequestDto dto);
    List<CommentResponseDto> getCommentsByTask(String taskId);
}

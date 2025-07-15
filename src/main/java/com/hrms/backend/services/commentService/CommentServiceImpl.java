package com.hrms.backend.services.commentService;

import com.hrms.backend.dtos.entityDtos.Comment.CommentRequestDto;
import com.hrms.backend.dtos.entityDtos.Comment.CommentResponseDto;
import com.hrms.backend.dtos.entityDtos.User.UserInfo;
import com.hrms.backend.dtos.entityDtos.User.response.UserResponseDto;
import com.hrms.backend.entities.Comment;
import com.hrms.backend.entities.User;
import com.hrms.backend.repositories.CommentRepository;
import com.hrms.backend.services.userService.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserServiceInterface userServiceInterface;

    @Override
    public CommentResponseDto addComment(String userId, CommentRequestDto dto) {
        Comment comment = Comment.builder()
            .taskId(dto.getTaskId())
            .userId(userId)
            .text(dto.getText())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        
        commentRepository.save(comment);
        return mapToDto(comment);
    }

    @Override
    public List<CommentResponseDto> getCommentsByTask(String taskId) {
        return commentRepository.findByTaskIdOrderByCreatedAtDesc(taskId).stream()
            .map(this::mapToDto)
            .toList();
    }

    private CommentResponseDto mapToDto(Comment comment) {
        UserResponseDto userById = userServiceInterface.getUserById(comment.getUserId());
        UserInfo build = UserInfo.builder().name(userById.getName()).imageUrl(userById.getImageUrl()).email(userById.getEmail()).id(userById.getId()).build();
        return CommentResponseDto.builder()
            .id(comment.getId())
            .taskId(comment.getTaskId())
            .text(comment.getText())
            .createdAt(comment.getCreatedAt())
            .updatedAt(comment.getUpdatedAt())
            .author(build)
            .build();
    }
}

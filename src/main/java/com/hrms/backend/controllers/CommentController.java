package com.hrms.backend.controllers;

import com.hrms.backend.dtos.entityDtos.Comment.CommentRequestDto;
import com.hrms.backend.dtos.entityDtos.Comment.CommentResponseDto;
import com.hrms.backend.security.JwtHelper;
import com.hrms.backend.services.commentService.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDto> addComment(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody @Valid CommentRequestDto dto
    ) {
        String userId = jwtHelper.getUserIdFromToken(authHeader.substring(7));
        return ResponseEntity.ok(commentService.addComment(userId, dto));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<List<CommentResponseDto>> getTaskComments(@PathVariable String taskId) {
        return ResponseEntity.ok(commentService.getCommentsByTask(taskId));
    }
}

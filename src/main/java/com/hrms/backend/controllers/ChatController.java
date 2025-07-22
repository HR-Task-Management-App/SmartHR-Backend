package com.hrms.backend.controllers;

import com.hrms.backend.dtos.entityDtos.ChatMessage.ChatMessageResponseDto;
import com.hrms.backend.dtos.entityDtos.ChatMessage.ChatResponseDto;
import com.hrms.backend.dtos.response_message.SuccessApiResponseMessage;
import com.hrms.backend.models.Chat;
import com.hrms.backend.models.ChatMessage;
import com.hrms.backend.repositories.ChatMessageRepository;
import com.hrms.backend.security.JwtHelper;
import com.hrms.backend.services.chatService.ChatService;
import com.hrms.backend.utils.EncryptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/chats")
public class ChatController {


    @Autowired
    private ChatService chatService;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private JwtHelper jwtHelper;

    @GetMapping("/history")
    public ResponseEntity<List<ChatMessageResponseDto>>getMessages(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam String otherUserId,
            @RequestParam String companyCode
    ) {
        String userId = jwtHelper.getUserIdFromToken(authHeader.substring(7));
        List<ChatMessageResponseDto> history = chatService.getHistory(userId, otherUserId, companyCode);
        return new ResponseEntity<>(history, HttpStatus.OK);
    }


    @GetMapping("/myChats")
    public ResponseEntity<List<ChatResponseDto>> getMyChats(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam  String companyCode
    ){
        String userId = jwtHelper.getUserIdFromToken(authHeader.substring(7));
        List<ChatResponseDto> chats = chatService.myChats(userId, companyCode);
        return new ResponseEntity<>(chats,HttpStatus.OK);
    }

    @PostMapping("/messageSeen")
    public ResponseEntity<SuccessApiResponseMessage> makeChatSeen(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam  String companyCode,
            @RequestParam  String otherUserId
            ){
        String userId = jwtHelper.getUserIdFromToken(authHeader.substring(7));
        SuccessApiResponseMessage successApiResponseMessage = chatService.makeChatSeen(userId, otherUserId, companyCode);
        return new ResponseEntity<>(successApiResponseMessage,HttpStatus.ACCEPTED);
    }
}

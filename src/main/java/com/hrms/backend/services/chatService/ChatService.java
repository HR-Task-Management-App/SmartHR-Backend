package com.hrms.backend.services.chatService;

import com.hrms.backend.dtos.entityDtos.ChatMessage.ChatMessageRequestDto;
import com.hrms.backend.dtos.entityDtos.ChatMessage.ChatMessageResponseDto;
import com.hrms.backend.dtos.entityDtos.ChatMessage.ChatResponseDto;
import com.hrms.backend.dtos.response_message.SuccessApiResponseMessage;
import com.hrms.backend.models.Chat;
import com.hrms.backend.models.ChatMessage;
import com.hrms.backend.models.enums.MessageStatus;
import com.hrms.backend.models.enums.MessageType;
import com.hrms.backend.repositories.ChatMessageRepository;
import com.hrms.backend.repositories.ChatRepository;
import com.hrms.backend.utils.EncryptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ChatService {

    @Autowired
    private ChatMessageRepository messageRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ModelMapper mapper;

    public List<ChatMessageResponseDto> getHistory(
            String userId,
            String otherUserId,
            String companyCode
    ){
        Sort sort = Sort.by(Sort.Direction.DESC, "timestamp");

        List<ChatMessage> messages = messageRepository.findChatHistoryBetweenUsers(
                companyCode, userId, otherUserId,sort
        );


        return messages.stream()
                .map(msg -> {
                    ChatMessageResponseDto responseDto = mapper.map(msg, ChatMessageResponseDto.class);
                    try {
                        responseDto.setContent(EncryptionUtil.decrypt(msg.getContent()));
                    } catch (Exception e) {
                        throw new RuntimeException("Decryption failed for message ID: " + msg.getId(), e);
                    }
                    return responseDto;
                })
                .toList();
    }


    public List<ChatResponseDto> myChats(String userId, String companyCode) {
        List<Chat> chatsForUser = chatRepository.findChatsForUser(companyCode, userId);
        return chatsForUser.stream().map(chat -> {
                    if (chat.getUser2().equals(userId)) {
                        String temp = chat.getUser1();
                        chat.setUser1(userId);
                        chat.setUser2(temp);
                    }
            try {
                chat.setLastMessage(EncryptionUtil.decrypt(chat.getLastMessage()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return mapper.map(chat, ChatResponseDto.class);
                }
        ).toList();
    }

    public SuccessApiResponseMessage makeChatSeen(String userId,String otherUserId,String companyCode){
        List<ChatMessage> chatHistoryBetweenUsers = messageRepository.findChatHistoryBetweenUsers(companyCode, userId, otherUserId, Sort.by(Sort.Direction.DESC,"timeStamp"));
        for (ChatMessage chat : chatHistoryBetweenUsers) {
            if (chat.getMessageStatus() != MessageStatus.SEEN) {
                chat.setMessageStatus(MessageStatus.SEEN);
                messageRepository.save(chat);
            }
        }
        Chat chat = chatRepository.findByUser1AndUser2(userId, otherUserId).orElse(null);
        if(chat!=null) {
            chat.setLastMessageStatus(MessageStatus.SEEN);
            chatRepository.save(chat);
        }
        return new SuccessApiResponseMessage("Successfully updates the message status");
    }

    public ChatMessageResponseDto saveMessage(ChatMessageRequestDto chatMessageRequestDto){
        String encrypted;
        try {
            encrypted = EncryptionUtil.encrypt(chatMessageRequestDto.getContent());
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed");
        }


        ChatMessage message = mapper.map(chatMessageRequestDto, ChatMessage.class);
        message.setTimestamp(LocalDateTime.now());
        message.setContent(encrypted);
        message.setMessageStatus(MessageStatus.DELIVERED);


        Chat chat = chatRepository.findByUser1AndUser2(chatMessageRequestDto.getSender(), chatMessageRequestDto.getReceiver()).orElse(new Chat());
        chat.setUser1(chatMessageRequestDto.getSender());
        chat.setUser2(chatMessageRequestDto.getReceiver());
        chat.setLastMessage(encrypted);
        chat.setLastUpdated(LocalDateTime.now());
        chat.setCompanyCode(chatMessageRequestDto.getCompanyCode());
        chat.setLastMessageType(MessageType.valueOf(chatMessageRequestDto.getMessageType()));
        chat.setLastMessageStatus(MessageStatus.DELIVERED); // reset seen status on new message
        Chat chat1 = chatRepository.save(chat);

        message.setChatId(chat1.getId());
        ChatMessage chatMessage = messageRepository.save(message);
        ChatMessageResponseDto responseDto = mapper.map(chatMessage, ChatMessageResponseDto.class);
        responseDto.setContent(chatMessageRequestDto.getContent());
        return responseDto;
    }

}

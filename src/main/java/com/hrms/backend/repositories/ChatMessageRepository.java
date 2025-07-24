package com.hrms.backend.repositories;

import com.hrms.backend.models.ChatMessage;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    @Query("{ 'companyCode': ?0, '$or': [ " +
            "{ 'sender': ?1, 'receiver': ?2 }, " +
            "{ 'sender': ?2, 'receiver': ?1 } ] }")
    List<ChatMessage> findChatHistoryBetweenUsers(String companyCode, String userId, String otherUserId, Sort sort);

    List<ChatMessage> findByChatIdAndMessageStatus(String chatId,String messageStatus);
}

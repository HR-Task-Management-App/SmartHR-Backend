package com.hrms.backend.repositories;

import com.hrms.backend.models.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findAllByCompanyCodeAndSenderIdAndReceiverIdOrderByTimestampAsc(
        String companyCode, String senderId, String receiverId);

    List<ChatMessage> findAllByReceiverIdAndDeliveredFalse(String receiverId);
}

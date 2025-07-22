package com.hrms.backend.repositories;

import com.hrms.backend.models.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends MongoRepository<Chat, String> {
    @Query("{ '$or': [ { 'user1': ?0, 'user2': ?1 }, { 'user1': ?1, 'user2': ?0 } ] }")
    Optional<Chat> findByUser1AndUser2(String user1, String user2);

    @Query("{ 'companyCode': ?0, $or: [ { 'user1': ?1 }, { 'user2': ?1 } ] }")
    List<Chat> findChatsForUser(String companyCode, String userId);

}

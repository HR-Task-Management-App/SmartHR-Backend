package com.hrms.backend.repositories;

import com.hrms.backend.entities.EmailConfirmationToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailConfirmationTokenRepository extends MongoRepository<EmailConfirmationToken, String> {
    EmailConfirmationToken findByToken(String token);
}

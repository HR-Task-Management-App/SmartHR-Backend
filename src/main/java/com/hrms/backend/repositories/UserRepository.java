package com.hrms.backend.repositories;

import com.hrms.backend.entities.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User,String> {
    Optional<User> findByEmail(String email); //To find user by email
    Optional<User> findByUserId(String userId);
}

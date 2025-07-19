package com.hrms.backend.repositories;

import com.hrms.backend.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User,String> {
    Optional<User> findByEmail(String email); //To find user by email
    List<User> findAllByIdIn(Collection<String> userIds);
    List<User> findAllByCompanyCodeAndIdIn(String companyCode, Collection<String> userIds);
}

package com.hrms.backend.repositories;

import com.hrms.backend.models.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment,String> {
    List<Comment> findByTaskIdOrderByCreatedAtDesc(String taskId);
}

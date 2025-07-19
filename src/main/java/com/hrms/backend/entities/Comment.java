package com.hrms.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "comments")
public class Comment {

    @Id
    private String id;

    private String taskId;

    private String userId;

    private String text;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Optional: for future nested replies
    private String parentId;

}

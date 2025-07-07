package com.hrms.backend.entities;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "email_tokens")
public class EmailConfirmationToken {
    @Id
    private String id;

    @Indexed(unique = true)
    private String token;

    @CreatedDate
    private LocalDateTime createdAt;

    @DBRef
    private User user;

    private String companyCode;

}

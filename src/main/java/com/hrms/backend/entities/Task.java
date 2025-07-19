package com.hrms.backend.entities;

import com.hrms.backend.entities.enums.Priority;
import com.hrms.backend.entities.enums.Status;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "tasks")
public class Task {

    @Id
    private String id;

    @Field(write = Field.Write.ALWAYS)
    private String imageUrl;

    private String companyCode;

    private String title;

    private String description;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    private Priority priority;

    private Status status = Status.NOT_STARTED; //By default NotStarted

    private String assignee; //HR id

    private Set<String> employees;

}

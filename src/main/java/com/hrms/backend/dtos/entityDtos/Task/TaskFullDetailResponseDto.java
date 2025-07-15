package com.hrms.backend.dtos.entityDtos.Task;

import com.hrms.backend.dtos.entityDtos.User.UserInfo;
import com.hrms.backend.entities.enums.Priority;
import com.hrms.backend.entities.enums.Status;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskFullDetailResponseDto {

    private String id;

    private String imageUrl;

    private String companyCode;

    private String title;

    private String description;

    private LocalDateTime updatedAt;

    private LocalDateTime createdAt;

    private Priority priority;

    private Status status; //By default NotStarted

    private UserInfo assignee; //id

    private List<UserInfo> employees;
}

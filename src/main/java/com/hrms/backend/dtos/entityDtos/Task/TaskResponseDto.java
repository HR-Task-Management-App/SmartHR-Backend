package com.hrms.backend.dtos.entityDtos.Task;

import com.hrms.backend.dtos.entityDtos.User.UserInfo;
import com.hrms.backend.entities.enums.Priority;
import com.hrms.backend.entities.enums.Status;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskResponseDto {

    private String id;

    private String imageUrl;

    private String companyCode;

    private String title;

    private String description;

    private Priority priority;

    private Status status; //By default NotStarted

    private UserInfo assignee; //id

    private List<UserInfo> employees;
}

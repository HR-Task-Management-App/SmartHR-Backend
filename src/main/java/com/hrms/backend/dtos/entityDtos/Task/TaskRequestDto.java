package com.hrms.backend.dtos.entityDtos.Task;

import com.hrms.backend.dtos.entityDtos.User.UserInfo;
import com.hrms.backend.entities.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class TaskRequestDto {

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 200,message = "Title must be between 3 and 200")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 1000,message = "Description must be between 10 and 1000")
    private String description;

    @NotBlank(message = "Priority is required")
    @Pattern(regexp = "LOW|MEDIUM|HIGH|URGENT", message = "Priority must be LOW, MEDIUM,HIGH or URGENT")
    private Priority priority;

    @NotBlank(message = "Please select employee to assign task")
    private Set<String> employees;

}

package com.hrms.backend.dtos.entityDtos.Task;

import com.hrms.backend.dtos.entityDtos.Task.Validators.UpdateTaskStatusValidator;
import com.hrms.backend.dtos.entityDtos.Task.Validators.UpdateTaskValidator;
import com.hrms.backend.entities.enums.Priority;
import com.hrms.backend.entities.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
    private String priority;

    @NotBlank(groups = UpdateTaskStatusValidator.class, message = "Status is required")
    @Pattern(regexp = "IN_PROGRESS|NOT_STARTED|FINISHED", message = "Priority must be IN_PROGRESS, NOT_STARTED, FINISHED")
    private String status;

    @NotEmpty(groups = UpdateTaskValidator.class,message = "Please select employee to assign task")
    private Set<String> employees;

}

package com.hrms.backend.services.taskService;


import com.hrms.backend.dtos.entityDtos.Task.TaskRequestDto;
import com.hrms.backend.dtos.entityDtos.Task.TaskResponseDto;
import org.springframework.web.multipart.MultipartFile;

public interface TaskServiceInterface {

    TaskResponseDto createTask(TaskRequestDto taskRequestDto, MultipartFile image, String hrId);
}

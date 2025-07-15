package com.hrms.backend.services.taskService;


import com.hrms.backend.dtos.entityDtos.Task.TaskRequestDto;
import com.hrms.backend.dtos.entityDtos.Task.TaskFullDetailResponseDto;
import com.hrms.backend.dtos.entityDtos.Task.TaskResponseDto;
import com.hrms.backend.dtos.response_message.SuccessApiResponseMessage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TaskServiceInterface {

    TaskFullDetailResponseDto createTask(TaskRequestDto taskRequestDto, MultipartFile image, String hrId);

    TaskResponseDto getTaskById(String taskId);

    List<TaskResponseDto> getTasks(String userId); //can be hr or employee

    TaskResponseDto updateTask(TaskRequestDto taskRequestDto, String taskId);

    SuccessApiResponseMessage deleteTask(String taskId);

    TaskResponseDto updateTaskStatus(TaskRequestDto taskRequestDto,String taskId);
}

package com.hrms.backend.services.taskService;


import com.hrms.backend.dtos.entityDtos.Task.TaskRequestDto;
import com.hrms.backend.dtos.entityDtos.Task.TaskResponseDto;
import com.hrms.backend.dtos.response_message.SuccessApiResponseMessage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TaskServiceInterface {

    TaskResponseDto createTask(TaskRequestDto taskRequestDto, MultipartFile image, String hrId);

    TaskResponseDto getTaskById(String taskId);

    List<TaskResponseDto> getCompanyTasks(String hrId); //hr

    List<TaskResponseDto> getEmployeeTasks(String userId); // employee

    TaskResponseDto updateTask(TaskRequestDto taskRequestDto, String taskId);

    SuccessApiResponseMessage deleteTask(String taskId);

    TaskResponseDto updateTaskStatus(TaskRequestDto taskRequestDto,String taskId);
}

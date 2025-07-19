package com.hrms.backend.controllers;

import com.hrms.backend.dtos.entityDtos.Task.TaskRequestDto;
import com.hrms.backend.dtos.entityDtos.Task.TaskResponseDto;
import com.hrms.backend.dtos.entityDtos.Task.Validators.UpdateTaskStatusValidator;
import com.hrms.backend.dtos.response_message.SuccessApiResponseMessage;
import com.hrms.backend.security.JwtHelper;
import com.hrms.backend.services.taskService.TaskServiceInterface;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static java.util.stream.Collectors.toMap;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private TaskServiceInterface taskServiceInterface;

    private final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(
            @RequestHeader("Authorization") String authHeader,
            @Valid @ModelAttribute TaskRequestDto taskRequestDto,
            @RequestParam(value = "image",required = false) MultipartFile image
    ) {
        String hrId = jwtHelper.getUserIdFromToken(authHeader.substring(7));
        TaskResponseDto task = taskServiceInterface.createTask(taskRequestDto, image, hrId);
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTaskById(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("id") String id
    ){
        TaskResponseDto taskById = taskServiceInterface.getTaskById(id);
        return new ResponseEntity<>(taskById,HttpStatus.OK);
    }

    @GetMapping("/companyTasks")
    public ResponseEntity<List<TaskResponseDto>> getCompanyTasks(
            @RequestHeader("Authorization") String authHeader
    ){
        String id = jwtHelper.getUserIdFromToken(authHeader.substring(7));
        List<TaskResponseDto> tasks = taskServiceInterface.getCompanyTasks(id);
        return new ResponseEntity<>(tasks,HttpStatus.OK);
    }

    @GetMapping("/userTasks")
    public ResponseEntity<List<TaskResponseDto>> getUserTasks(
            @RequestHeader("Authorization") String authHeader
    ){
        String id = jwtHelper.getUserIdFromToken(authHeader.substring(7));
        List<TaskResponseDto> tasks = taskServiceInterface.getEmployeeTasks(id);
        return new ResponseEntity<>(tasks,HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTask(
            @RequestHeader("Authorization") String authHeader,
            @Validated({Default.class,UpdateTaskStatusValidator.class}) @RequestBody TaskRequestDto taskRequestDto,
            @PathVariable("id") String id
    ){
        TaskResponseDto taskResponseDto = taskServiceInterface.updateTask(taskRequestDto, id);
        return new ResponseEntity<>(taskResponseDto,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessApiResponseMessage> deleteTask(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("id") String id
            ){
        SuccessApiResponseMessage successApiResponseMessage = taskServiceInterface.deleteTask(id);
        return new ResponseEntity<>(successApiResponseMessage,HttpStatus.OK);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<TaskResponseDto> updateTaskStatus(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("id") String id,
            @Validated({UpdateTaskStatusValidator.class}) @RequestBody TaskRequestDto taskRequestDto
            ){
        TaskResponseDto taskResponseDto = taskServiceInterface.updateTaskStatus(taskRequestDto, id);
        return new ResponseEntity<>(taskResponseDto,HttpStatus.OK);
    }
}

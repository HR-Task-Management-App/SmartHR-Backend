package com.hrms.backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrms.backend.dtos.entityDtos.Task.TaskRequestDto;
import com.hrms.backend.dtos.entityDtos.Task.TaskResponseDto;
import com.hrms.backend.entities.enums.Priority;
import com.hrms.backend.security.JwtHelper;
import com.hrms.backend.services.taskService.TaskServiceInterface;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.DataInput;
import java.io.IOException;

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
             @RequestParam(value = "data",required = false) String data,
             @RequestParam(value = "image",required = false) MultipartFile image
    ) throws JsonProcessingException {
//        logger.info(image.getContentType());
        String hrId = jwtHelper.getUserIdFromToken(authHeader.substring(7));
        ObjectMapper objectMapper = new ObjectMapper();
        TaskRequestDto taskRequestDto = objectMapper.readValue(data, TaskRequestDto.class);
        TaskResponseDto task = taskServiceInterface.createTask(taskRequestDto, image, hrId);
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }
}

package com.hrms.backend.exceptions;

import com.hrms.backend.dtos.response_message.ErrorApiResponseMessage;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //handing resource not found custom exception
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorApiResponseMessage> resourceNotFoundExceptionHandler(ResourceNotFoundException ex){
        ErrorApiResponseMessage response = ErrorApiResponseMessage.builder().error(ex.getMessage()).build();
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    //handling api not valid data
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", ")); // join messages with comma

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", errorMessage);

        return ResponseEntity.badRequest().body(errorResponse);
    }

    //handling bad request custom exception
    @ExceptionHandler(BadApiRequestException.class)
    public ResponseEntity<ErrorApiResponseMessage> resourceNotFoundExceptionHandler(BadApiRequestException ex){
        ErrorApiResponseMessage response = ErrorApiResponseMessage.builder().error(ex.getMessage()).build();
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
}

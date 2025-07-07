package com.hrms.backend.exceptions;

import com.hrms.backend.dtos.response_message.ErrorApiResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Map<String,Object>> apiNotValidDataExceptionHandler(MethodArgumentNotValidException ex){
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();// this gives all the error in data which is  coming
        Map<String,Object> response = new HashMap<>();

        for (ObjectError er : allErrors){
            String message = er.getDefaultMessage();
            String field = ((FieldError) er).getField();
            response.put(field,message);
        }
        return  new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    //handling bad request custom exception
    @ExceptionHandler(BadApiRequestException.class)
    public ResponseEntity<ErrorApiResponseMessage> resourceNotFoundExceptionHandler(BadApiRequestException ex){
        ErrorApiResponseMessage response = ErrorApiResponseMessage.builder().error(ex.getMessage()).build();
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
}

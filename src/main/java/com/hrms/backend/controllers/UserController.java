package com.hrms.backend.controllers;


import com.hrms.backend.dtos.entityDtos.UserDto;
import com.hrms.backend.services.userService.UserServiceInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserServiceInterface userServiceInterface;


    @PostMapping //create
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
        UserDto user =  userServiceInterface.createUser(userDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}

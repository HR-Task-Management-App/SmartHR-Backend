package com.hrms.backend.controllers;


import com.hrms.backend.dtos.entityDtos.User.UserDto;
import com.hrms.backend.security.JwtHelper;
import com.hrms.backend.services.userService.UserServiceInterface;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserServiceInterface userServiceInterface;

    Logger logger = LoggerFactory.getLogger(CompanyController.class);
    @Autowired
    private JwtHelper jwtHelper;

    @PostMapping //create
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
        UserDto user =  userServiceInterface.createUser(userDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<UserDto> getUser(
            @RequestHeader("Authorization") String authHeader
    ){
        String userId = jwtHelper.getUserIdFromToken(authHeader.substring(7));
        return new ResponseEntity<>(userServiceInterface.getUserById(userId),HttpStatus.OK);
    }
}

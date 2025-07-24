package com.hrms.backend.controllers;

import com.hrms.backend.dtos.entityDtos.User.response.UserResponseDto;
import com.hrms.backend.dtos.response_message.SuccessApiResponseMessage;
import com.hrms.backend.security.JwtHelper;
import com.hrms.backend.services.userService.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ping")
public class PingController {

    @Autowired
    private UserServiceInterface userServiceInterface;

    @Autowired
    private JwtHelper jwtHelper;

    @GetMapping
    private SuccessApiResponseMessage getUserProfileForPing(
            @RequestHeader("Authorization") String authHeader
    ){
        String userId = jwtHelper.getUserIdFromToken(authHeader.substring(7));
        UserResponseDto userById = userServiceInterface.getUserById(userId);
        return new SuccessApiResponseMessage("Ping success");
    }
}

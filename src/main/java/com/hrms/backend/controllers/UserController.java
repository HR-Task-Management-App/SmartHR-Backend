package com.hrms.backend.controllers;


import com.hrms.backend.dtos.entityDtos.User.request.CreateUserRequestDto;
import com.hrms.backend.dtos.entityDtos.User.request.UserUpdateProfileRequestDto;
import com.hrms.backend.dtos.entityDtos.User.response.UserResponseDto;
import com.hrms.backend.dtos.response_message.SuccessApiResponseMessage;
import com.hrms.backend.security.JwtHelper;
import com.hrms.backend.services.userService.UserServiceInterface;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Validated
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserServiceInterface userServiceInterface;

    Logger logger = LoggerFactory.getLogger(CompanyController.class);
    @Autowired
    private JwtHelper jwtHelper;

    @PostMapping //create
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody CreateUserRequestDto createUserRequestDto){
        UserResponseDto user =  userServiceInterface.createUser(createUserRequestDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<UserResponseDto> getUserProfile(
            @RequestHeader("Authorization") String authHeader
    ){
        String userId = jwtHelper.getUserIdFromToken(authHeader.substring(7));
        return new ResponseEntity<>(userServiceInterface.getUserById(userId),HttpStatus.OK);
    }

    @PostMapping("/profile-image")
    public ResponseEntity<SuccessApiResponseMessage> updateProfileImage(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("image") MultipartFile image
    ){
        String userId = jwtHelper.getUserIdFromToken(authHeader.substring(7));
        return new ResponseEntity<>(userServiceInterface.updateProfileImage(image, userId),HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<UserResponseDto> updateProfile(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody @Valid UserUpdateProfileRequestDto userUpdateProfileRequestDto
    ){
        String userId = jwtHelper.getUserIdFromToken(authHeader.substring(7));
        UserResponseDto userResponseDto = userServiceInterface.updateUser(userUpdateProfileRequestDto, userId);
        return new ResponseEntity<>(userResponseDto,HttpStatus.ACCEPTED);
    }

    @PatchMapping("/{companyCode}") //ROLE_USER
    public ResponseEntity<SuccessApiResponseMessage> updateCompanyCode(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String companyCode
            ){
        String userId = jwtHelper.getUserIdFromToken(authHeader.substring(7));
        SuccessApiResponseMessage successApiResponseMessage = userServiceInterface.updateCompanyCode(companyCode, userId);
        return new ResponseEntity<>(successApiResponseMessage,HttpStatus.ACCEPTED);
    }

    @PatchMapping("/leave-company")//ROLE_USER
    public ResponseEntity<SuccessApiResponseMessage> leaveCurrentCompany(
            @RequestHeader("Authorization") String authHeader
    ){
        String userId = jwtHelper.getUserIdFromToken(authHeader.substring(7));
        SuccessApiResponseMessage successApiResponseMessage = userServiceInterface.leaveCurrentCompany(userId);
        return new ResponseEntity<>(successApiResponseMessage,HttpStatus.ACCEPTED);
    }

    @PatchMapping("/remove-wait-company")//ROLE_USER
    public ResponseEntity<SuccessApiResponseMessage> removeFromWaitlist(
            @RequestHeader("Authorization") String authHeader
    ){
        String userId = jwtHelper.getUserIdFromToken(authHeader.substring(7));
        SuccessApiResponseMessage successApiResponseMessage = userServiceInterface.retrieveFromWaitlistCompany(userId);
        return new ResponseEntity<>(successApiResponseMessage,HttpStatus.ACCEPTED);
    }

}

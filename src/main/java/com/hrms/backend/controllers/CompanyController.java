package com.hrms.backend.controllers;

import com.hrms.backend.dtos.entityDtos.User.UserListResponse;
import com.hrms.backend.dtos.response_message.SuccessApiResponseMessage;
import com.hrms.backend.security.JwtHelper;
import com.hrms.backend.services.companyService.CompanyServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    Logger logger = LoggerFactory.getLogger(CompanyController.class);
    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private CompanyServiceInterface companyServiceInterface;

    @GetMapping("/empWaitlist")
    public ResponseEntity<UserListResponse> getCompanyWaitlistEmployee(
            @RequestHeader("Authorization") String authHeader
    ){
        String hrId = jwtHelper.getUserIdFromToken(authHeader.substring(7));
        UserListResponse waitlistEmployeesId = companyServiceInterface.getWaitlistEmployees(hrId);
        logger.info("HrId = {}", hrId);
        return new  ResponseEntity<>(waitlistEmployeesId,HttpStatus.OK);
    }

    @GetMapping("/employees")
    public ResponseEntity<UserListResponse> getEmployeesOfCompany(
            @RequestHeader("Authorization") String authHeader
    ){
        String hrId = jwtHelper.getUserIdFromToken(authHeader.substring(7));
        UserListResponse employeesOfCompany = companyServiceInterface.getEmployeesOfCompany(hrId);
        logger.info("HrId = {}", hrId);
        return new  ResponseEntity<>(employeesOfCompany,HttpStatus.OK);
    }

    @PostMapping("/acceptEmployee/{userId}")
    public ResponseEntity<SuccessApiResponseMessage> acceptEmployeeToJoinCompany(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String userId
    ){
        String hrId = jwtHelper.getUserIdFromToken(authHeader.substring(7));
        SuccessApiResponseMessage successApiResponseMessage = companyServiceInterface.acceptEmployee(hrId, userId);
        return new ResponseEntity<>(successApiResponseMessage,HttpStatus.ACCEPTED);
    }

    @PostMapping("/rejectEmployee/{userId}")
    public ResponseEntity<SuccessApiResponseMessage> rejectEmployeeFromJoiningCompany(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String userId
    ){
        String hrId = jwtHelper.getUserIdFromToken(authHeader.substring(7));
        SuccessApiResponseMessage successApiResponseMessage = companyServiceInterface.rejectEmployee(hrId, userId);
        return new ResponseEntity<>(successApiResponseMessage,HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/removeEmployee/{userId}")
    public ResponseEntity<SuccessApiResponseMessage> removeEmployeeFromCompany(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String userId
    ){
        String hrId = jwtHelper.getUserIdFromToken(authHeader.substring(7));
        SuccessApiResponseMessage successApiResponseMessage = companyServiceInterface.removeEmployeeFromCompany(hrId, userId);
        return new ResponseEntity<>(successApiResponseMessage,HttpStatus.ACCEPTED);
    }
}

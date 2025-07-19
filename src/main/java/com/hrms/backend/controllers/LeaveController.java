package com.hrms.backend.controllers;

import com.hrms.backend.dtos.entityDtos.Leave.EmployeeLeaveResponseDto;
import com.hrms.backend.dtos.entityDtos.Leave.HRLeaveResponseDto;
import com.hrms.backend.dtos.entityDtos.Leave.LeaveRequestDto;
import com.hrms.backend.dtos.response_message.SuccessApiResponseMessage;
import com.hrms.backend.security.JwtHelper;
import com.hrms.backend.services.leaveService.LeaveServiceInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leaves")
public class LeaveController {

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private LeaveServiceInterface leaveServiceInterface;

    @PostMapping
    public ResponseEntity<EmployeeLeaveResponseDto> submitLeaveRequest(
        @Valid @RequestBody LeaveRequestDto leaveRequestDto,
        @RequestHeader("Authorization") String auth
    ) {
        String userIdFromToken = jwtHelper.getUserIdFromToken(auth.substring(7));
        EmployeeLeaveResponseDto employeeLeaveResponseDto = leaveServiceInterface.submitLeaveRequest(leaveRequestDto, userIdFromToken);
        return new ResponseEntity<>(employeeLeaveResponseDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EmployeeLeaveResponseDto>> getEmployeeLeaveDetails(
            @RequestHeader("Authorization") String auth
    ){
        String userIdFromToken = jwtHelper.getUserIdFromToken(auth.substring(7));
        List<EmployeeLeaveResponseDto> employeeLeavesDetailById = leaveServiceInterface.getEmployeeLeavesDetailById(userIdFromToken);
        return new ResponseEntity<>(employeeLeavesDetailById,HttpStatus.OK);
    }

    @GetMapping("/company")
    public ResponseEntity<List<HRLeaveResponseDto>> getCompanyLeaveRequests(
            @RequestHeader("Authorization") String auth
    ){
        String hrId = jwtHelper.getUserIdFromToken(auth.substring(7));
        List<HRLeaveResponseDto> leaveRequestsOfCompany = leaveServiceInterface.getLeaveRequestsOfCompany(hrId);
        return new ResponseEntity<>(leaveRequestsOfCompany,HttpStatus.OK);
    }

    @PostMapping("/{leaveId}")
    public ResponseEntity<EmployeeLeaveResponseDto> updateLeaveRequestDetail(
            @PathVariable("leaveId") String leaveId,
            @Valid @RequestBody LeaveRequestDto leaveRequestDto,
            @RequestHeader("Authorization") String auth
    ){
        String userId = jwtHelper.getUserIdFromToken(auth.substring(7));
        EmployeeLeaveResponseDto employeeLeaveResponseDto = leaveServiceInterface.updateLeaveRequestDetail(leaveRequestDto, userId, leaveId);
        return new ResponseEntity<>(employeeLeaveResponseDto,HttpStatus.OK);
    }

    @PostMapping("/status/{leaveId}/{status}") //hr
    private ResponseEntity<SuccessApiResponseMessage> updateStatusOfLeave(
            @PathVariable("leaveId") String leaveId,
            @PathVariable("status") String status,
            @RequestHeader("Authorization") String auth
    ){
        String hrId = jwtHelper.getUserIdFromToken(auth.substring(7));
        SuccessApiResponseMessage successApiResponseMessage = leaveServiceInterface.updateStatusOfLeaveRequest(leaveId, hrId, status);
        return new ResponseEntity<>(successApiResponseMessage,HttpStatus.OK);
    }

    @PostMapping("/response/{leaveId}")
    private ResponseEntity<SuccessApiResponseMessage> removeHRResponseFromLeave(
            @PathVariable("leaveId") String leaveId,
            @RequestHeader("Authorization") String auth

    ){
        String hrId = jwtHelper.getUserIdFromToken(auth.substring(7));
        SuccessApiResponseMessage successApiResponseMessage = leaveServiceInterface.removeHRRespondToLeaveRequest(hrId, leaveId);
        return new ResponseEntity<>(successApiResponseMessage,HttpStatus.OK);
    }
}

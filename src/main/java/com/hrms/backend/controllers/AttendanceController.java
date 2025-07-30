package com.hrms.backend.controllers;

import com.hrms.backend.dtos.entityDtos.Attendance.AttendanceRequestDto;
import com.hrms.backend.dtos.entityDtos.Attendance.AttendanceResponseDto;
import com.hrms.backend.security.JwtHelper;
import com.hrms.backend.services.attendanceService.AttendanceServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/attendances")
public class AttendanceController {

    @Autowired
    private AttendanceServiceInterface attendanceService;

    @Autowired
    private JwtHelper jwtHelper;

    // Endpoint to mark check-in or check-out
    @PostMapping
    public AttendanceResponseDto markAttendance(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody AttendanceRequestDto attendanceRequestDto
    ) {
        String userId = jwtHelper.getUserIdFromToken(authHeader.substring(7));
        return attendanceService.markEmployeeAttendance(userId, attendanceRequestDto);
    }

    // Get attendance history of a specific employee
    @GetMapping("/history")
    public List<AttendanceResponseDto> getEmployeeAttendanceHistory(
            @RequestHeader("Authorization") String authHeader
            ) {
        String userId = jwtHelper.getUserIdFromToken(authHeader.substring(7));
        return attendanceService.getEmployeeAttendanceHistory(userId);
    }

    // HR: Get company-wide attendance for a specific date (optional)
    @GetMapping("/company")
    public List<AttendanceResponseDto> getCompanyAttendanceByDate(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(name = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> date
    ) {
        String hrId = jwtHelper.getUserIdFromToken(authHeader.substring(7));
        return attendanceService.getCompanyAttendanceByDate(hrId, date);
    }
}


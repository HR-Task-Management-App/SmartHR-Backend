package com.hrms.backend.controllers;

import com.hrms.backend.dtos.entityDtos.Office.OfficeLocationRequestDto;
import com.hrms.backend.dtos.entityDtos.Office.OfficeLocationResponseDto;
import com.hrms.backend.security.JwtHelper;
import com.hrms.backend.services.officeService.OfficeServiceInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/offices")
public class OfficeController {

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private OfficeServiceInterface officeServiceInterface;

    @PostMapping
    public ResponseEntity<OfficeLocationResponseDto> createOffice(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody OfficeLocationRequestDto officeLocationRequestDto
            ){
        String hrId = jwtHelper.getUserIdFromToken(authHeader.substring(7));
        OfficeLocationResponseDto officeLocation = officeServiceInterface.createOfficeLocation(officeLocationRequestDto, hrId);
        return ResponseEntity.ok(officeLocation);
    }

    @PutMapping("/{officeId}")
    public ResponseEntity<OfficeLocationResponseDto> updateOfficeDetails(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("officeId") String officeId,
            @Valid @RequestBody OfficeLocationRequestDto officeLocationRequestDto
    ){
        String hrId = jwtHelper.getUserIdFromToken(authHeader.substring(7));
        OfficeLocationResponseDto officeLocation = officeServiceInterface.updateOfficeLocation(officeLocationRequestDto,officeId,hrId);
        return ResponseEntity.ok(officeLocation);
    }


    @GetMapping
    public ResponseEntity<OfficeLocationResponseDto> getOfficeDetails(
            @RequestHeader("Authorization") String authHeader
    ){
        String userId = jwtHelper.getUserIdFromToken(authHeader.substring(7));
        OfficeLocationResponseDto officeLocation = officeServiceInterface.getOfficeDetail(userId);
        return ResponseEntity.ok(officeLocation);
    }

}

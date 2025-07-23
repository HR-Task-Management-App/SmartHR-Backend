package com.hrms.backend.controllers;

import com.hrms.backend.dtos.entityDtos.Meeting.MeetingRequestDto;
import com.hrms.backend.dtos.entityDtos.Meeting.MeetingResponseDto;
import com.hrms.backend.dtos.response_message.SuccessApiResponseMessage;
import com.hrms.backend.security.JwtHelper;
import com.hrms.backend.services.meetingService.MeetingServiceInterface;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meetings")
public class MeetingController {

    @Autowired
    private MeetingServiceInterface meetingServiceInterface;

    @Autowired
    private JwtHelper jwtHelper;

    @PostMapping("/create")
    public ResponseEntity<MeetingResponseDto> create(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody MeetingRequestDto dto
    ) {
        String hrId = jwtHelper.getUserIdFromToken(authHeader.substring(7));
        return ResponseEntity.ok(meetingServiceInterface.scheduleMeeting(hrId,dto));
    }

    @GetMapping("/myMeetings")
    public ResponseEntity<List<MeetingResponseDto>> myMeetings(
            @RequestHeader("Authorization") String authHeader
    ) {
        String userId = jwtHelper.getUserIdFromToken(authHeader.substring(7));
        return new ResponseEntity<>(meetingServiceInterface.getMyMeetings(userId), HttpStatus.CREATED);
    }

    @PostMapping("/respond/{meetingId}")
    public ResponseEntity<SuccessApiResponseMessage> respond(
        @PathVariable String meetingId,
        @RequestHeader("Authorization") String authHeader,
        @RequestParam String status
    ) {
        String userId = jwtHelper.getUserIdFromToken(authHeader.substring(7));
        SuccessApiResponseMessage successApiResponseMessage = meetingServiceInterface.userRespondToMeeting(meetingId, userId, status);
        return ResponseEntity.ok(successApiResponseMessage);
    }

    @PostMapping("/cancel/{meetingId}")
    public ResponseEntity<SuccessApiResponseMessage> cancel(
            @PathVariable String meetingId,
            @RequestHeader("Authorization") String authHeader
            ) {
        String hrId = jwtHelper.getUserIdFromToken(authHeader.substring(7));
        SuccessApiResponseMessage successApiResponseMessage = meetingServiceInterface.cancelMeeting(meetingId, hrId);
        return ResponseEntity.ok(successApiResponseMessage);
    }

    @GetMapping("/{meetingId}")
    public ResponseEntity<MeetingResponseDto> getMeetingById(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String meetingId
    ){
        String userId = jwtHelper.getUserIdFromToken(authHeader.substring(7));
        MeetingResponseDto meetingById = meetingServiceInterface.getMeetingById(meetingId, userId);
        return new ResponseEntity<>(meetingById,HttpStatus.OK);
    }

    @PostMapping("/{meetingId}")
    public ResponseEntity<MeetingResponseDto> editMeetingDetails(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody MeetingRequestDto meetingRequestDto,
            @PathVariable String meetingId
    ){
        String userId = jwtHelper.getUserIdFromToken(authHeader.substring(7));
        MeetingResponseDto meeting = meetingServiceInterface.editMeetingDetails(meetingRequestDto,meetingId, userId);
        return new ResponseEntity<>(meeting,HttpStatus.OK);
    }
}

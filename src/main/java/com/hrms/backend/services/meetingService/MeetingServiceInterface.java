package com.hrms.backend.services.meetingService;

import com.hrms.backend.dtos.entityDtos.Meeting.MeetingRequestDto;
import com.hrms.backend.dtos.entityDtos.Meeting.MeetingResponseDto;
import com.hrms.backend.dtos.response_message.SuccessApiResponseMessage;

import java.util.List;

public interface MeetingServiceInterface {

    MeetingResponseDto scheduleMeeting(String hrId, MeetingRequestDto dto);

    List<MeetingResponseDto> getMyMeetings(String userId);

    SuccessApiResponseMessage userRespondToMeeting(String meetingId, String userId, String status);

    SuccessApiResponseMessage cancelMeeting(String meetingId, String hrId);
}

package com.hrms.backend.services.meetingService;

import com.hrms.backend.dtos.entityDtos.Meeting.MeetingRequestDto;
import com.hrms.backend.dtos.entityDtos.Meeting.MeetingResponseDto;
import com.hrms.backend.dtos.response_message.SuccessApiResponseMessage;
import com.hrms.backend.exceptions.BadApiRequestException;
import com.hrms.backend.exceptions.ResourceNotFoundException;
import com.hrms.backend.models.Meeting;
import com.hrms.backend.models.User;
import com.hrms.backend.models.MeetingResponse;
import com.hrms.backend.models.enums.MeetingStatus;
import com.hrms.backend.models.enums.ResponseStatus;
import com.hrms.backend.models.enums.Role;
import com.hrms.backend.repositories.MeetingRepository;
import com.hrms.backend.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MeetingServiceImpl implements MeetingServiceInterface {


    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    //HR
    public MeetingResponseDto scheduleMeeting(String hrId,MeetingRequestDto dto) {
        User user = userRepository.findById(hrId).orElseThrow(() -> new BadApiRequestException("Not authorized to create meetings"));
        for (String participant : dto.getParticipants()) {
            boolean conflict = meetingRepository.existsByCompanyCodeAndParticipantsContainsAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
                    user.getCompanyCode(), participant, LocalDateTime.parse(dto.getEndTime()), LocalDateTime.parse(dto.getStartTime())
            );
            if (conflict) {
                throw new BadApiRequestException("Time conflict for participant: " + participant);
            }
        }

        Set<String> participants = dto.getParticipants();
        //taking only valid users present in database and same company
        List<User> users = userRepository.findAllByCompanyCodeAndIdIn(user.getCompanyCode(),participants);
        Meeting meeting = mapper.map(dto, Meeting.class);
        meeting.setParticipants(users.stream().map(User::getId).collect(Collectors.toSet()));
        meeting.setOrganizer(hrId);
        meeting.setCompanyCode(user.getCompanyCode());

        Meeting savedMeeting = meetingRepository.save(meeting);
        return mapper.map(savedMeeting,MeetingResponseDto.class);
    }


    public List<MeetingResponseDto> getMyMeetings(String userId) {
        log.debug("Into get my meetings");
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not valid to respond to meeting"));
        if (user.getRole().equals(Role.ROLE_HR)){
        List<Meeting> meetings = meetingRepository.findByCompanyCodeAndOrganizer(user.getCompanyCode(), userId);
            return meetings.stream().map(meeting -> mapper.map(meeting, MeetingResponseDto.class)
            ).toList();
        }
        List<Meeting> meetings = meetingRepository.findByCompanyCodeAndParticipantsContains(user.getCompanyCode(), userId);
        return meetings.stream().map(meeting -> mapper.map(meeting, MeetingResponseDto.class)
        ).toList();
    }

    public SuccessApiResponseMessage userRespondToMeeting(String meetingId, String userId, String status) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new BadApiRequestException("Meeting not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not valid to respond to meeting"));

        if (!status.equals("DECLINED") && !status.equals("ACCEPTED")) {
            throw new BadApiRequestException("Status can be only ACCEPTED or DECLINED");
        }

        if (!user.getRole().equals(Role.ROLE_USER)) {
            throw new BadApiRequestException("Only employees can respond to meetings");
        }

        if (!meeting.getParticipants().contains(userId)) {
            throw new BadApiRequestException("You are not part of this meeting");
        }

        // Ensure responses list is not null
        if (meeting.getResponses() == null) {
            meeting.setResponses(new ArrayList<>());
        }

        Optional<MeetingResponse> existingResponse = meeting.getResponses().stream()
                .filter(r -> r.getParticipant().equals(userId))
                .findFirst();

        if (existingResponse.isPresent()) {
            existingResponse.get().setStatus(ResponseStatus.valueOf(status));
        } else {
            meeting.getResponses().add(new MeetingResponse(userId, ResponseStatus.valueOf(status)));
        }

        meetingRepository.save(meeting);
        return new SuccessApiResponseMessage("Status updated successfully");
    }



    //HR
    public SuccessApiResponseMessage cancelMeeting(String meetingId, String userId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new BadApiRequestException("Meeting not found"));
        if (!meeting.getOrganizer().equals(userId)) {
            throw new BadApiRequestException("Unauthorized to cancel this meeting.");
        }
        meeting.setStatus(MeetingStatus.CANCELLED);
        meetingRepository.save(meeting);
        return new SuccessApiResponseMessage("Meeting cancelled successfully");
    }

    public MeetingResponseDto getMeetingById(String meetingId,String userId){
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new BadApiRequestException("Meeting not found"));

        if (!meeting.getOrganizer().equals(userId) && !meeting.getParticipants().contains(userId)) {
            throw new BadApiRequestException("You are not authorized to get the meeting details");
        }
        return mapper.map(meeting, MeetingResponseDto.class);
    }

    public MeetingResponseDto editMeetingDetails(MeetingRequestDto meetingRequestDto,String meetingId,String userId){
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new BadApiRequestException("Meeting not found"));
        if (!meeting.getOrganizer().equals(userId)) {
            throw new BadApiRequestException("Unauthorized to edit this meeting.");
        }
        mapper.map(meetingRequestDto,meeting);
        return mapper.map(meeting, MeetingResponseDto.class);
    }



}

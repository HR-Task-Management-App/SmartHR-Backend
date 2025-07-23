package com.hrms.backend.dtos.entityDtos.Meeting;

import com.hrms.backend.dtos.entityDtos.User.UserInfo;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingResponseDto {
    private String id;
    private String title;
    private String description;
    private String organizer;
    private String companyCode;
    private String meetingLink;
    private String startTime;
    private String endTime;
    private Set<UserInfo> participants;
    private List<MeetingResponseInfo> responses;
    private String status;
}

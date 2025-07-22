package com.hrms.backend.dtos.entityDtos.Meeting;

import com.hrms.backend.dtos.entityDtos.User.UserInfo;
import com.hrms.backend.models.enums.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MeetingResponseInfo{
    private UserInfo participant;
    private ResponseStatus status;
}
package com.hrms.backend.dtos.entityDtos.Leave;

import com.hrms.backend.dtos.entityDtos.User.UserInfo;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HRLeaveResponseDto {

    private String id; //leave Id

    private UserInfo employee;

    private String type;

    private String emergencyContact;

    private String startDate;

    private String endDate;

    private String leaveDescription;

    private String status;

    private String appliedAt;

    private String respondedAt;

    private UserInfo responseBy; // hr id
}

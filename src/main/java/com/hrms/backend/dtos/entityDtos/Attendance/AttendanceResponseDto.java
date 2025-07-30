package com.hrms.backend.dtos.entityDtos.Attendance;

import com.hrms.backend.dtos.entityDtos.User.UserInfo;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AttendanceResponseDto {
    public String id;
    public UserInfo employee;
    public String companyCode;
    public String latitude;
    public String longitude;
    private String checkIn;
    private String checkOut;
}

package com.hrms.backend.dtos.entityDtos.Office;

import com.hrms.backend.dtos.entityDtos.User.UserInfo;
import lombok.*;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OfficeLocationResponseDto {
    private String id;
    private String companyCode;
    private UserInfo createdBy;
    private String latitude;
    private String longitude;
    private String radius;
    private String createdAt;
    private String updatedAt;
}

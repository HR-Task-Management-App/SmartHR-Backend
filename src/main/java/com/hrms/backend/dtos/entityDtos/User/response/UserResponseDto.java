package com.hrms.backend.dtos.entityDtos.User.response;

import com.hrms.backend.entities.enums.Department;
import com.hrms.backend.entities.enums.Position;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private String id;

    private String name;

    private String email;

    private String phone;

    private String gender;

    private String role;

    private Position position;

    private Department department;

    private String waitingCompanyCode;

    //only for employee, for hr we will assign it auto.
    private String companyCode;

    private String imageUrl;

}

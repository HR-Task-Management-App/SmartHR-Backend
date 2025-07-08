package com.hrms.backend.dtos.responseDtos.company;


import lombok.*;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CompanyWaitlistEmployees {

    private String companyCode;

    private Set<String> waitlistEmployeeIds;
}

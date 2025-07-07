package com.hrms.backend.dtos.entityDtos;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyDto {

    private String id;

    private String companyCode;

    @Size(min = 3,max = 100,message = " Company name should be between 3 to 100 character")
    private String companyName;
}

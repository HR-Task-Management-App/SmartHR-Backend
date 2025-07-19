package com.hrms.backend.dtos.entityDtos.Leave;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveRequestDto {

    @NotNull(message = "Please fill the Leave type")
    @Pattern(regexp = "SICK|CASUAL|UNPAID|VACATION|MATERNITY|PATERNITY|OTHERS", message = "Priority must be SICK, CASUAL, UNPAID, VACATION, MATERNITY, PATERNITY, OTHERS")
    private String type;

    @NotNull(message = "Please fill the emergency contact")
    @Pattern(
            regexp = "^\\+\\d{1,4}\\d{10}$",
            message = "Phone number must start with a country code (e.g., +91) followed by exactly 10 digits"
    )
    private String emergencyContact; // e.g., "+919876543210"

    @NotNull(message = "Please fill the Start date")
    @Pattern(
            regexp = "^\\d{4}-\\d{2}-\\d{2}$",
            message = "Start date must be in the format yyyy-MM-dd"
    )
    private String startDate; // e.g., "2025-07-20"

    @NotNull(message = "Please fill the End date")
    @Pattern(
            regexp = "^\\d{4}-\\d{2}-\\d{2}$",
            message = "End date must be in the format yyyy-MM-dd"
    )
    private String endDate;

    @NotNull(message = "Please fill the leave description")
    @Size(min = 10,max=1000,message = "Description should be more than 10 and less than 1000 character")
    private String leaveDescription;

}


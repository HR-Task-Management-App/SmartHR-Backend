package com.hrms.backend.dtos.entityDtos.Attendance;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AttendanceRequestDto {

    @NotNull(message = "Type is required")
    @Pattern(regexp = "CHECKIN|CHECKOUT", message = "Type must be CHECKIN or CHECKOUT")
    public String type;

    @NotNull(message = "Latitude is required")
    @NotBlank(message = "Latitude must not be blank")
    public String latitude;

    @NotNull(message = "Longitude is required")
    @NotBlank(message = "Longitude must not be blank")
    public String longitude;
}


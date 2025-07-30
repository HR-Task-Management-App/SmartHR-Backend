package com.hrms.backend.dtos.entityDtos.Office;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfficeLocationRequestDto {

    @NotNull(message = "Latitude is required")
    @NotBlank(message = "Latitude must not be blank")
    private String latitude;

    @NotNull(message = "Longitude is required")
    @NotBlank(message = "Longitude must not be blank")
    private String longitude;

    @NotNull(message = "Radius is required")
    @NotBlank(message = "Radius must not be blank")
    private String radius; // in meters
}

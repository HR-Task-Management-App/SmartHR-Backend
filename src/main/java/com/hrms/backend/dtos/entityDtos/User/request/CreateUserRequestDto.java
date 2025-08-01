package com.hrms.backend.dtos.entityDtos.User.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequestDto {

    @NotBlank(message = "Name is required")
    @Size(min = 3,max = 50,message = "Name should be between 3 to 50 character")
    private String name;

    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",message = "Invalid User email")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^\\+\\d{1,4}\\d{10}$",
            message = "Phone number must start with a country code (e.g., +91) followed by exactly 10 digits"
    )
    private String phone;

    @NotBlank(message = "Password is required")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // only in request body
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@#$%^&+=!]{6,}$",
            message = "Password must be at least 6 characters and include numbers"
    )
    private String password;


    @NotBlank(message = "Role is required")
    @Pattern(
            regexp = "ROLE_USER|ROLE_HR",
            message = "Role must be one of: ROLE_USER, or ROLE_HR"
    )
    private String role;


    @Size(min = 7,max = 7,message = "Company code must be exactly 7 character")
    //only for employee, for hr we will assign it auto.
    private String companyCode;

}

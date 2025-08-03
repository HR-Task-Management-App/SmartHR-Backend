package com.hrms.backend.dtos.entityDtos.LoginSignUp;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoogleSignUpRequest {

    @NotNull(message = "token is required")
    @NotBlank(message = "token cannot be empty")
    private String idToken;
    @NotBlank(message = "Role is required")
    @Pattern(
            regexp = "ROLE_USER|ROLE_HR",
            message = "Role must be one of: ROLE_USER, or ROLE_HR"
    )
    private String role;
}

package com.hrms.backend.dtos.entityDtos.LoginSignUp;


import com.hrms.backend.dtos.entityDtos.User.response.UserResponseDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {

    private String token;
    UserResponseDto user;
}

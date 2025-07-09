package com.hrms.backend.dtos.token_request_response;


import com.hrms.backend.dtos.entityDtos.User.UserDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {

    private String token;
    UserDto user;
}

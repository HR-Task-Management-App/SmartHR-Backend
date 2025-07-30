package com.hrms.backend.dtos.entityDtos.LoginSignUp;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoogleLoginRequest {
    private String idToken;
}

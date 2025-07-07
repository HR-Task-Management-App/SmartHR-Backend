package com.hrms.backend.dtos.response_message;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorApiResponseMessage {
    private String error;
}

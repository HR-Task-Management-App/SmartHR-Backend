package com.hrms.backend.models;

import com.hrms.backend.models.enums.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MeetingResponse {
    private String participant;
    private ResponseStatus status; // PENDING, ACCEPTED, DECLINED
}

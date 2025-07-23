package com.hrms.backend.dtos.entityDtos.Meeting;

import com.hrms.backend.Validations.annotations.ValidDateTimeFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MeetingRequestDto {

    @NotBlank(message = "Title is required.")
    @Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters.")
    private String title;

    @Size(max = 1000, message = "Description can't exceed 1000 characters.")
    private String description;

    @NotBlank(message = "Start time is required.")
    @ValidDateTimeFormat
    private String startTime;

    @NotBlank(message = "Meeting link is required")
    private String meetingLink;

    @NotBlank(message = "End time is required.")
    @ValidDateTimeFormat
    private String endTime;

    @NotNull(message = "Participants list is required.")
    @Size(min = 1, message = "At least one participant is required.")
    private Set<String> participants;

}

package com.hrms.backend.models;

import com.hrms.backend.models.enums.MeetingStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "meetings")
public class Meeting {
    @Id
    private String id;
    private String title;
    private String description;
    private String organizer; // User who created the meeting // HR
    private String companyCode;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Set<String> participants; // employees
    @Field(write = Field.Write.ALWAYS )
    private List<MeetingResponse> responses; // status of each participant
    private MeetingStatus status = MeetingStatus.SCHEDULED; // SCHEDULED, CANCELLED, COMPLETED
}

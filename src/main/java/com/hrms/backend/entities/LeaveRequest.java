package com.hrms.backend.entities;

import com.hrms.backend.entities.enums.LeaveStatus;
import com.hrms.backend.entities.enums.LeaveType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "leaveRequests")
public class LeaveRequest {
    
    @Id
    private String id;

    private String userId; // employee

    private String companyCode;

    private LeaveType type;

    private String emergencyContact;

    private LocalDate startDate; // yyyy-MM-dd

    private LocalDate endDate; // yyyy-MM-dd

    private String leaveDescription;

    private LeaveStatus status = LeaveStatus.PENDING;

    private LocalDateTime appliedAt = LocalDateTime.now();

    @Field(write = Field.Write.ALWAYS)
    private LocalDateTime respondedAt; // approved or rejected

    @Field(write = Field.Write.ALWAYS)
    private String respondedBy; // hr id when approved or rejected
}

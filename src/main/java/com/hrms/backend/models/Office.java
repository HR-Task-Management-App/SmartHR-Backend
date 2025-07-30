package com.hrms.backend.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "offices")
public class Office {
    @Id
    private String id;
    private String companyCode;
    private String createdBy;
    private String latitude;
    private String longitude;
    private String radius;
    private Instant createdAt;
    private Instant updatedAt;
}

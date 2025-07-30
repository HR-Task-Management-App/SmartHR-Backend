package com.hrms.backend.models;

import com.hrms.backend.models.enums.AttendanceType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "attendances")
public class Attendance {
    
    @Id
    public String id;
    public String employee;
    public String companyCode;
    public AttendanceType type; // "CHECKIN" or "CHECKOUT"
    public String latitude;
    public String longitude;
    @Field(write  = Field.Write.ALWAYS)
    public Instant checkIn;
    @Field(write  = Field.Write.ALWAYS)
    public Instant checkOut;
}

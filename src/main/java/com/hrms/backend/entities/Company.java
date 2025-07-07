package com.hrms.backend.entities;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "companies")
public class Company {

    @Id
    private ObjectId id;

    private String companyCode;

    private String companyName;

    @CreatedDate
    private String createdDate;

    private ObjectId hr;

    private List<ObjectId> employees;
}

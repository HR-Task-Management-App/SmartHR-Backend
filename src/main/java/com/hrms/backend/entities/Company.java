package com.hrms.backend.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "companies")
public class Company {

    @Id
    private String id;

    private String companyCode;

    @Field(write = Field.Write.ALWAYS)
    private String companyName;

    private String createdDate;

    private String hr;

    @Field(write = Field.Write.ALWAYS)
    private Set<String> employees = new HashSet<>();

    @Field(write = Field.Write.ALWAYS)
    private Set<String> waitListEmployees = new HashSet<>(); // employee that HR does not still confirm
}

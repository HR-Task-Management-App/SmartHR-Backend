package com.hrms.backend.entities;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    private String hrId;

    @Field(write = Field.Write.ALWAYS)
    private Set<String> employeesId = new HashSet<>();

    @Field(write = Field.Write.ALWAYS)
    private Set<String> waitListEmployeesId = new HashSet<>(); // employee that HR does not still confirm
}

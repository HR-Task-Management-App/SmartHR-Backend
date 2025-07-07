package com.hrms.backend.repositories;

import com.hrms.backend.entities.Company;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends MongoRepository<Company,String> {
    Optional<Company> findByCompanyCode(String companyCode);
}

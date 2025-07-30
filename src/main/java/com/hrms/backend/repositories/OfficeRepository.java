package com.hrms.backend.repositories;

import com.hrms.backend.models.Office;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OfficeRepository extends MongoRepository<Office,String> {
    Optional<Office> findByCompanyCode(String companyCode);
    Optional<Office> findByCreatedBy(String hrId);
}

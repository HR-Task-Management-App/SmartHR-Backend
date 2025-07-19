package com.hrms.backend.repositories;

import com.hrms.backend.entities.LeaveRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRepository extends MongoRepository<LeaveRequest,String> {
    List<LeaveRequest> findAllByUserIdAndCompanyCode(String userId,String companyCode); // employee
    List<LeaveRequest> findAllByCompanyCode(String companyCode);
}

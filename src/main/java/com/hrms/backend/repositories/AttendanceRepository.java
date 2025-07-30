package com.hrms.backend.repositories;

import com.hrms.backend.models.Attendance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends MongoRepository<Attendance,String> {
    Optional<Attendance> findByEmployeeAndCheckInBetween(String userId, Instant start, Instant end);
    List<Attendance> findByEmployee(String userId);
    List<Attendance> findByCompanyCodeAndCheckInBetween(String companyCode, Instant start, Instant end);
}

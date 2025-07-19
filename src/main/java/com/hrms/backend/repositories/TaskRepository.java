package com.hrms.backend.repositories;

import com.hrms.backend.entities.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends MongoRepository<Task,String> {
    List<Task> findAllByCompanyCode(String companyCode);
    List<Task> findAllByCompanyCodeAndEmployeesContaining(String companyCode, String userId);

}

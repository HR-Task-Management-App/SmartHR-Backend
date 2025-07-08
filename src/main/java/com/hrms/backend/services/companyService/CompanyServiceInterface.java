package com.hrms.backend.services.companyService;

import com.hrms.backend.dtos.responseDtos.company.CompanyWaitlistEmployees;
import com.hrms.backend.dtos.response_message.SuccessApiResponseMessage;
import org.bson.types.ObjectId;

import java.util.List;

public interface CompanyServiceInterface {

    CompanyWaitlistEmployees getWaitlistEmployeesId(String hrUserId);

    SuccessApiResponseMessage acceptEmployee(String hrUserId,String empUserId);

    SuccessApiResponseMessage rejectEmployee(String hrUserId,String empUserId);

    SuccessApiResponseMessage removeEmployeeFromCompany(String hrUserId,String empUserId);
}

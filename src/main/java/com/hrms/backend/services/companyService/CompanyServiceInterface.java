package com.hrms.backend.services.companyService;

import com.hrms.backend.dtos.entityDtos.User.UserInfo;
import com.hrms.backend.dtos.entityDtos.User.UserListResponse;
import com.hrms.backend.dtos.response_message.SuccessApiResponseMessage;

import java.util.List;

public interface CompanyServiceInterface {

    UserListResponse getWaitlistEmployees(String hrUserId);

    UserListResponse getEmployeesOfCompany(String hrUserId);

    SuccessApiResponseMessage acceptEmployee(String hrUserId,String empUserId);

    SuccessApiResponseMessage rejectEmployee(String hrUserId,String empUserId);

    SuccessApiResponseMessage removeEmployeeFromCompany(String hrUserId,String empUserId);

    List<UserInfo> getEveryBodyOfCompany(String userId); //get every employee as well as hr of company // chatting

}

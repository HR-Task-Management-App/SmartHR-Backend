package com.hrms.backend.services.leaveService;

import com.hrms.backend.dtos.entityDtos.Leave.EmployeeLeaveResponseDto;
import com.hrms.backend.dtos.entityDtos.Leave.HRLeaveResponseDto;
import com.hrms.backend.dtos.entityDtos.Leave.LeaveRequestDto;
import com.hrms.backend.dtos.response_message.SuccessApiResponseMessage;

import java.util.List;

public interface LeaveServiceInterface {

    EmployeeLeaveResponseDto submitLeaveRequest(LeaveRequestDto leaveRequestDto,String userId); //employee
    List<EmployeeLeaveResponseDto> getEmployeeLeavesDetailById(String userId); // employee
    List<HRLeaveResponseDto> getLeaveRequestsOfCompany(String hrId); //hr
    EmployeeLeaveResponseDto updateLeaveRequestDetail(LeaveRequestDto leaveRequestDto,String userId,String leaveRequestId); // emp
    SuccessApiResponseMessage updateStatusOfLeaveRequest(String leaveId,String hrId,String status); //hr
    SuccessApiResponseMessage removeHRRespondToLeaveRequest(String hrId,String leaveId);
}

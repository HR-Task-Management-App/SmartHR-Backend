package com.hrms.backend.services.leaveService;

import com.hrms.backend.dtos.entityDtos.Leave.EmployeeLeaveResponseDto;
import com.hrms.backend.dtos.entityDtos.Leave.HRLeaveResponseDto;
import com.hrms.backend.dtos.entityDtos.Leave.LeaveRequestDto;
import com.hrms.backend.dtos.entityDtos.User.UserInfo;
import com.hrms.backend.dtos.entityDtos.User.response.UserResponseDto;
import com.hrms.backend.dtos.response_message.SuccessApiResponseMessage;
import com.hrms.backend.entities.LeaveRequest;
import com.hrms.backend.entities.enums.LeaveStatus;
import com.hrms.backend.exceptions.BadApiRequestException;
import com.hrms.backend.exceptions.ResourceNotFoundException;
import com.hrms.backend.repositories.LeaveRepository;
import com.hrms.backend.services.userService.UserServiceInterface;
import com.hrms.backend.utils.DateValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LeaveServiceImple implements LeaveServiceInterface {

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserServiceInterface userServiceInterface;


    @Override
    public EmployeeLeaveResponseDto submitLeaveRequest(LeaveRequestDto leaveRequestDto,String userId) {
        UserResponseDto userById = userServiceInterface.getUserById(userId);
        if(userById.getCompanyCode()==null || userById.getCompanyCode().isBlank()){
            throw new BadApiRequestException("User is not connected to any company");
        }
        DateValidator.startEndDateValidator(leaveRequestDto.getStartDate(),leaveRequestDto.getEndDate());
        LeaveRequest leaveRequest = modelMapper.map(leaveRequestDto, LeaveRequest.class);
        leaveRequest.setUserId(userId);
        leaveRequest.setCompanyCode(userById.getCompanyCode());
        LeaveRequest save = leaveRepository.save(leaveRequest);
        return modelMapper.map(save, EmployeeLeaveResponseDto.class);
    }

    @Override
    public List<EmployeeLeaveResponseDto> getEmployeeLeavesDetailById(String userId) {
        UserResponseDto userById1 = userServiceInterface.getUserById(userId);
        List<LeaveRequest> requests = leaveRepository.findAllByUserIdAndCompanyCode(userId,userById1.getCompanyCode());
        return requests.stream().map(leaveRequest -> {
            EmployeeLeaveResponseDto employeeLeaveResponseDto = modelMapper.map(leaveRequest, EmployeeLeaveResponseDto.class);
            if (leaveRequest.getRespondedBy() != null) {
                UserResponseDto userById = userServiceInterface.getUserById(leaveRequest.getRespondedBy());
                UserInfo build = UserInfo.builder().name(userById.getName()).email(userById.getEmail()).id(userById.getId()).imageUrl(userById.getImageUrl()).build();
                employeeLeaveResponseDto.setResponseBy(build);
            }
            return employeeLeaveResponseDto;
        }).toList();
    }

    @Override
    public List<HRLeaveResponseDto> getLeaveRequestsOfCompany(String hrId) {
        UserResponseDto user = userServiceInterface.getUserById(hrId);
        List<LeaveRequest> allByCompanyCode = leaveRepository.findAllByCompanyCode(user.getCompanyCode());
        return allByCompanyCode.stream().map(leaveRequest -> {
            HRLeaveResponseDto hrLeaveResponseDto = modelMapper.map(leaveRequest, HRLeaveResponseDto.class);
            if (leaveRequest.getRespondedBy() != null) {
                UserResponseDto userById = userServiceInterface.getUserById(leaveRequest.getRespondedBy());
                UserInfo build = UserInfo.builder().name(userById.getName()).email(userById.getEmail()).id(userById.getId()).imageUrl(userById.getImageUrl()).build();
                hrLeaveResponseDto.setResponseBy(build);
            }
            UserResponseDto userById = userServiceInterface.getUserById(leaveRequest.getUserId());
            UserInfo build = UserInfo.builder().name(userById.getName()).email(userById.getEmail()).imageUrl(userById.getImageUrl()).id(userById.getId()).build();
            hrLeaveResponseDto.setEmployee(build);
            return hrLeaveResponseDto;
        }).toList();
    }

    @Override
    public EmployeeLeaveResponseDto updateLeaveRequestDetail(
            LeaveRequestDto leaveRequestDto,
            String userId,
            String leaveRequestId
    ) {
        UserResponseDto userById = userServiceInterface.getUserById(userId);
        LeaveRequest leaveRequest = leaveRepository.findById(leaveRequestId).orElseThrow(() -> new ResourceNotFoundException("Leave request not found!!"));
        if (userById.getCompanyCode() == null || !userById.getCompanyCode().equals(leaveRequest.getCompanyCode())) {
            throw new BadApiRequestException("You are not authorized to update this leave request (company mismatch)");
        }

        if (!leaveRequest.getUserId().equals(userId)) {
            throw new BadApiRequestException("You are not authorized to update this leave request (user mismatch)");
        }
        if(leaveRequest.getRespondedBy()!=null){
            throw new BadApiRequestException("You are not authorized to update, HR already responded to this leave request");
        }
        modelMapper.map(leaveRequestDto,leaveRequest);
        LeaveRequest save = leaveRepository.save(leaveRequest);
        return modelMapper.map(save, EmployeeLeaveResponseDto.class);
    }

    @Override
    public SuccessApiResponseMessage updateStatusOfLeaveRequest(String leaveId, String hrId,String status) {
        if(!status.equals("APPROVED") && !status.equals("REJECTED")){
            throw new BadApiRequestException("Status can be approved, rejected");
        }
        UserResponseDto userById = userServiceInterface.getUserById(hrId);
        LeaveRequest leaveRequest = leaveRepository.findById(leaveId).orElseThrow(() -> new ResourceNotFoundException("Leave request not found!!"));
        if (userById.getCompanyCode() == null || !userById.getCompanyCode().equals(leaveRequest.getCompanyCode())) {
            throw new BadApiRequestException("You are not authorized to update this leave request (company mismatch)");
        }
        leaveRequest.setRespondedAt(LocalDateTime.now());
        leaveRequest.setRespondedBy(hrId);
        leaveRequest.setStatus(LeaveStatus.valueOf(status));
        leaveRepository.save(leaveRequest);
        return new SuccessApiResponseMessage("Status successfully changes to "+status);
    }

    @Override
    public SuccessApiResponseMessage removeHRRespondToLeaveRequest(String hrId, String leaveId) {
        UserResponseDto userById = userServiceInterface.getUserById(hrId);
        LeaveRequest leaveRequest = leaveRepository.findById(leaveId).orElseThrow(() -> new ResourceNotFoundException("Leave request not found!!"));
        if (userById.getCompanyCode() == null || !userById.getCompanyCode().equals(leaveRequest.getCompanyCode())) {
            throw new BadApiRequestException("You are not authorized to update this leave request (company mismatch)");
        }
        leaveRequest.setRespondedBy(null);
        leaveRequest.setRespondedAt(null);
        leaveRepository.save(leaveRequest);
        return new SuccessApiResponseMessage("Removed your response from the the leave request");
    }


}

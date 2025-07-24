package com.hrms.backend.services.companyService;

import com.hrms.backend.dtos.entityDtos.User.UserInfo;
import com.hrms.backend.dtos.entityDtos.User.UserListResponse;
import com.hrms.backend.dtos.response_message.SuccessApiResponseMessage;
import com.hrms.backend.exceptions.BadApiRequestException;
import com.hrms.backend.exceptions.ResourceNotFoundException;
import com.hrms.backend.models.Company;
import com.hrms.backend.models.User;
import com.hrms.backend.repositories.CompanyRepository;
import com.hrms.backend.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CompanyServiceImpl implements CompanyServiceInterface {


    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public UserListResponse getWaitlistEmployees(String hrUserId) {
        Company company = companyRepository.findByHr(hrUserId).orElseThrow(()-> new BadApiRequestException("Company does not exist!!"));
        Set<String> waitListEmployeesId = company.getWaitListEmployees();

        List<User> users = userRepository.findAllByIdIn(waitListEmployeesId);

        List<UserInfo> userInfos = users.stream()
                .map(user -> UserInfo.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .imageUrl(user.getImageUrl())
                        .build())
                .toList();

        return UserListResponse.builder()
                .companyCode(company.getCompanyCode())
                .users(userInfos)
                .build();
    }

    @Override
    public UserListResponse getEmployeesOfCompany(String hrUserId) {
        Company company = companyRepository.findByHr(hrUserId).orElseThrow(()-> new BadApiRequestException("Company does not exist!!"));
        Set<String> employeesId = company.getEmployees();
        List<User> users = userRepository.findAllByIdIn(employeesId);

        List<UserInfo> userInfos = users.stream()
                .map(user -> UserInfo.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .imageUrl(user.getImageUrl())
                        .build())
                .toList();

        return UserListResponse.builder()
                .companyCode(company.getCompanyCode())
                .users(userInfos)
                .build();
    }

    @Override
    public SuccessApiResponseMessage acceptEmployee(String hrUserId,String empUserId) {
        Company company = companyRepository.findByHr(hrUserId).orElseThrow(()-> new BadApiRequestException("Company does not exist!!"));
        User user = userRepository.findById(empUserId).orElseThrow(()-> new BadApiRequestException("User does not exits"));
        if(company.getWaitListEmployees().contains(empUserId)){
            company.getEmployees().add(empUserId);
            company.getWaitListEmployees().remove(empUserId);
            Company save = companyRepository.save(company);
            user.setCompanyCode(save.getCompanyCode());
            user.setWaitingCompanyCode(null);
            userRepository.save(user);
        }
        else{
            throw new BadApiRequestException("This Employee does exits in company's waitlist");
        }
        return new SuccessApiResponseMessage("Employee successfully added to the company");
    }

    @Override
    public SuccessApiResponseMessage rejectEmployee(String hrUserId,String empUserId) {
        Company company = companyRepository.findByHr(hrUserId).orElseThrow(()-> new BadApiRequestException("Company does not exist!!"));
        User user = userRepository.findById(empUserId).orElseThrow(()-> new BadApiRequestException("User does not exits"));
        if(company.getWaitListEmployees().contains(empUserId)){
            user.setWaitingCompanyCode(null);
            company.getWaitListEmployees().remove(empUserId);
            companyRepository.save(company);
            userRepository.save(user);
        }
        else{
            throw new BadApiRequestException("This Employee does exits in company's waitlist");
        }
        return new SuccessApiResponseMessage("Employee successfully rejected from joining the company");
    }

    @Override
    public SuccessApiResponseMessage removeEmployeeFromCompany(String hrUserId,String empUserId) {
        Company company = companyRepository.findByHr(hrUserId).orElseThrow(()-> new BadApiRequestException("Company does not exist!!"));
        User user = userRepository.findById(empUserId).orElseThrow(()-> new BadApiRequestException("User does not exits"));
        if(company.getEmployees().contains(empUserId)){
            company.getEmployees().remove(empUserId);
            user.setCompanyCode(null);
            userRepository.save(user);
            companyRepository.save(company);
        }
        else{
            throw new BadApiRequestException("This Employee does exits in the company");
        }
        return new SuccessApiResponseMessage("Employee successfully removed from the company");
    }

    @Override
    public List<UserInfo> getEveryBodyOfCompany(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found!!"));
        String companyCode = user.getCompanyCode();
        companyRepository.findByCompanyCode(companyCode).orElseThrow(()-> new ResourceNotFoundException("Company does not exist!!"));
        List<User> allUsers = userRepository.findAllByCompanyCode(companyCode);
        return allUsers.stream().map(user1->
                UserInfo.builder().name(user1.getName())
                        .id(user1.getId())
                        .email(user1.getEmail())
                        .imageUrl(user1.getImageUrl())
                        .build()
        ).toList();
    }
}

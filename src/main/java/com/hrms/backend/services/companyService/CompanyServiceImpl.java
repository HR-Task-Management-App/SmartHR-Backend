package com.hrms.backend.services.companyService;

import com.hrms.backend.dtos.responseDtos.company.CompanyWaitlistEmployees;
import com.hrms.backend.dtos.response_message.SuccessApiResponseMessage;
import com.hrms.backend.entities.Company;
import com.hrms.backend.entities.User;
import com.hrms.backend.exceptions.BadApiRequestException;
import com.hrms.backend.repositories.CompanyRepository;
import com.hrms.backend.repositories.UserRepository;
import com.hrms.backend.security.JwtHelper;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyServiceInterface {


    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public CompanyWaitlistEmployees getWaitlistEmployeesId(String hrUserId) {
        Company company = companyRepository.findByHrId(hrUserId).orElseThrow(()-> new BadApiRequestException("Company does not exist!!"));
        return CompanyWaitlistEmployees.builder().
                companyCode(company.getCompanyCode()).
                waitlistEmployeeIds(company.getWaitListEmployeesId()).build();
    }

    @Override
    public SuccessApiResponseMessage acceptEmployee(String hrUserId,String empUserId) {
        Company company = companyRepository.findByHrId(hrUserId).orElseThrow(()-> new BadApiRequestException("Company does not exist!!"));
        User user = userRepository.findByUserId(empUserId).orElseThrow(()-> new BadApiRequestException("User does not exits"));
        if(company.getWaitListEmployeesId().contains(empUserId)){
            company.getEmployeesId().add(empUserId);
            company.getWaitListEmployeesId().remove(empUserId);
            Company save = companyRepository.save(company);
            user.setCompanyCode(save.getCompanyCode());
            userRepository.save(user);
        }
        else{
            throw new BadApiRequestException("This Employee does exits in company's waitlist");
        }
        return new SuccessApiResponseMessage("Employee successfully added to the company");
    }

    @Override
    public SuccessApiResponseMessage rejectEmployee(String hrUserId,String empUserId) {
        Company company = companyRepository.findByHrId(hrUserId).orElseThrow(()-> new BadApiRequestException("Company does not exist!!"));
        if(company.getWaitListEmployeesId().contains(empUserId)){
            company.getWaitListEmployeesId().remove(empUserId);
            companyRepository.save(company);
        }
        else{
            throw new BadApiRequestException("This Employee does exits in company's waitlist");
        }
        return new SuccessApiResponseMessage("Employee successfully rejected from joining the company");
    }

    @Override
    public SuccessApiResponseMessage removeEmployeeFromCompany(String hrUserId,String empUserId) {
        Company company = companyRepository.findByHrId(hrUserId).orElseThrow(()-> new BadApiRequestException("Company does not exist!!"));
        User user = userRepository.findByUserId(empUserId).orElseThrow(()-> new BadApiRequestException("User does not exits"));
        if(company.getEmployeesId().contains(empUserId)){
            company.getEmployeesId().remove(empUserId);
            user.setCompanyCode(null);
            userRepository.save(user);
            companyRepository.save(company);
        }
        else{
            throw new BadApiRequestException("This Employee does exits in the company");
        }
        return new SuccessApiResponseMessage("Employee successfully removed from the company");
    }


}

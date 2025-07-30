package com.hrms.backend.services.officeService;

import com.hrms.backend.dtos.entityDtos.Office.OfficeLocationRequestDto;
import com.hrms.backend.dtos.entityDtos.Office.OfficeLocationResponseDto;
import com.hrms.backend.exceptions.BadApiRequestException;
import com.hrms.backend.exceptions.ResourceNotFoundException;
import com.hrms.backend.models.Company;
import com.hrms.backend.models.Office;
import com.hrms.backend.models.User;
import com.hrms.backend.repositories.CompanyRepository;
import com.hrms.backend.repositories.OfficeRepository;
import com.hrms.backend.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class OfficeServiceImpl implements OfficeServiceInterface {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public OfficeLocationResponseDto createOfficeLocation(OfficeLocationRequestDto officeLocationRequestDto, String hrId) {
        User user = userRepository.findById(hrId).orElseThrow(() -> new ResourceNotFoundException("User is not valid!!"));
        Company company = companyRepository.findByHr(hrId).orElseThrow(()-> new ResourceNotFoundException("Company of user is not valid!!"));
        boolean present = officeRepository.findByCreatedBy(hrId).isPresent();
        if(present){
            throw new BadApiRequestException("Office already assigned to HR, you can update the details of office");
        }
        Office office = Office.builder()
                .createdBy(hrId)
                .companyCode(company.getCompanyCode())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .longitude(officeLocationRequestDto.getLongitude())
                .latitude(officeLocationRequestDto.getLatitude())
                .radius(officeLocationRequestDto.getRadius())
                .build();

        Office savedOffice = officeRepository.save(office);
        return mapper.map(savedOffice,OfficeLocationResponseDto.class);
    }

    public OfficeLocationResponseDto updateOfficeLocation(OfficeLocationRequestDto officeLocationRequestDto,String officeId,String hrId){
        Office office = officeRepository.findById(officeId).orElseThrow(() -> new ResourceNotFoundException("Office is not valid!!"));
        if(!office.getCreatedBy().equals(hrId)){
            throw new ResourceNotFoundException("Not a valid request");
        }
        office.setUpdatedAt(Instant.now());
        mapper.map(officeLocationRequestDto,office);
        Office savedOffice = officeRepository.save(office);
        return mapper.map(savedOffice, OfficeLocationResponseDto.class);
    }

    public OfficeLocationResponseDto getOfficeDetail(String userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User is not valid"));
        Office office = officeRepository.findByCompanyCode(user.getCompanyCode()).orElseThrow(() -> new ResourceNotFoundException("No office found for this company"));
        return mapper.map(office,OfficeLocationResponseDto.class);
    }
}

package com.hrms.backend.services.officeService;

import com.hrms.backend.dtos.entityDtos.Office.OfficeLocationRequestDto;
import com.hrms.backend.dtos.entityDtos.Office.OfficeLocationResponseDto;

public interface OfficeServiceInterface {

    OfficeLocationResponseDto createOfficeLocation(OfficeLocationRequestDto officeLocationRequestDto,String hrId);
    OfficeLocationResponseDto updateOfficeLocation(OfficeLocationRequestDto officeLocationRequestDto,String officeId,String hrId);
    OfficeLocationResponseDto getOfficeDetail(String userId);
}

package com.hrms.backend.services.userService;


import com.hrms.backend.dtos.entityDtos.User.request.CreateUserRequestDto;
import com.hrms.backend.dtos.entityDtos.User.request.UserUpdateProfileRequestDto;
import com.hrms.backend.dtos.entityDtos.User.response.UserResponseDto;
import com.hrms.backend.dtos.response_message.SuccessApiResponseMessage;
import org.springframework.web.multipart.MultipartFile;

public interface UserServiceInterface {

    //create
    UserResponseDto createUser(CreateUserRequestDto createUserRequestDto);
    //update
    UserResponseDto updateUser(UserUpdateProfileRequestDto userUpdateProfileRequestDto, String userId);

    //getUserById
    UserResponseDto getUserById(String userId);

    SuccessApiResponseMessage updateProfileImage(MultipartFile file, String userId);

    SuccessApiResponseMessage updateCompanyCode(String companyCode, String userId);

    SuccessApiResponseMessage leaveCurrentCompany(String userId);

    SuccessApiResponseMessage retrieveFromWaitlistCompany(String userId);
  //delete
//    void deleteUser(String userId) throws IOException;




}

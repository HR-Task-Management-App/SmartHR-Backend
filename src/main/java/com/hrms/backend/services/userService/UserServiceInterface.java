package com.hrms.backend.services.userService;


import com.hrms.backend.dtos.entityDtos.UserDto;

import java.io.IOException;
import java.util.List;

public interface UserServiceInterface {

    //create
    UserDto createUser(UserDto userDto);

//    //update
//    UserDto updateUser(UserDto userDto,String userId);
//
//    //delete
//    void deleteUser(String userId) throws IOException;
//
//    //getAllUser
////    PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir);
//
//    //getUserById
//    UserDto getUserById(String userId);
//
//    //getUserByEmail
//    UserDto getUserByEmail(String email);
//
//    //getUserBySearchKeyword
////    List<UserDto> getUserByKeyword(String keyword);

}

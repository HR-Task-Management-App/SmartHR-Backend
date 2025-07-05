package com.hrms.backend.services.userService;

import com.hrms.backend.dtos.entityDtos.UserDto;
import com.hrms.backend.entities.User;
import com.hrms.backend.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Primary
public class UserServiceImplementation implements UserServiceInterface {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public UserDto createUser(UserDto userDto) {
        User user = dtoToEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User savedUser = userRepository.save(user);
        return entityToDto(savedUser);
    }


//    @Override
//    public UserDto updateUser(UserDto userDto, String userId) {
//        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found"));
//        user.setImageName(userDto.getImageName());
//        user.setName(userDto.getName());
//        user.setGender(userDto.getGender());
//        user.setAbout(userDto.getAbout());
//        userRepository.save(user);
//        return entityToDto(user);
//    }

//    @Override
//    public void deleteUser(String userId) {
//        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found"));
//        //deleting user image
//        String imageName = user.getImageName();
//        String fullImagePath = imagePath+ File.separator+imageName;
//        try { //  below deleting image of user
//            // sometimes it give file cant be access error
//            // but when we will upload in server this error will be gone
//            Path path = Paths.get(fullImagePath);
//            Files.delete(path);
//        }
//        catch (Exception e){ // if file does not exist
//            System.out.println(e.getMessage()+" No Such File exist");
//        }
//        //deleting user
//        userRepository.deleteById(userId);
//    }

//    @Override
//    public PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir) {
//
//        //Sorting
//        Sort sort = sortDir.equalsIgnoreCase("desc")? Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
//        //Pagination
//        Pageable pageable = PageRequest.of(pageNumber, pageSize,sort);
//        Page<User> page = userRepository.findAll(pageable);
//        PageableResponse<UserDto> pagableResponse = Helper.getPagableResponse(page, UserDto.class);
//
//        return pagableResponse;
//    }

//    @Override
//    public UserDto getUserById(String userId) {
//        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found"));
//        return entityToDto(user);
//    }

//    @Override
//    public UserDto getUserByEmail(String email) {
//        User user = userRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("Email not found"));
//        return entityToDto(user);
//    }

//    @Override
//    public List<UserDto> getUserByKeyword(String keyword) {
//        List<User> user = userRepository.findByNameContaining(keyword);
//        List<UserDto> userDto = new ArrayList<>();
//        for(User u : user){
//            userDto.add(entityToDto(u));
//        }
//        return userDto;
//    }


    private UserDto entityToDto(User savedUser) {
        return modelMapper.map(savedUser,UserDto.class);
    }

    private User dtoToEntity(UserDto userDto) {
        return modelMapper.map(userDto,User.class);
    }
}

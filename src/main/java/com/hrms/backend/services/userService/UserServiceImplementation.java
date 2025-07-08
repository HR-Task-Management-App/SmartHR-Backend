package com.hrms.backend.services.userService;

import com.hrms.backend.dtos.entityDtos.UserDto;
import com.hrms.backend.entities.Company;
import com.hrms.backend.entities.Role;
import com.hrms.backend.entities.User;
import com.hrms.backend.exceptions.BadApiRequestException;
import com.hrms.backend.exceptions.ResourceNotFoundException;
import com.hrms.backend.repositories.CompanyRepository;
import com.hrms.backend.repositories.UserRepository;
import com.hrms.backend.utils.CodeGenerator;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImplementation implements UserServiceInterface {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new BadApiRequestException("User with this email already exists!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now().toString());

        if (user.getRole() == Role.ROLE_HR) {
            String companyCode = CodeGenerator.generateBase64Code();
            while (companyRepository.findByCompanyCode(companyCode).isPresent()) {
                companyCode = CodeGenerator.generateBase64Code();
            }
            user.setCompanyCode(companyCode);

            User savedUser = userRepository.save(user);
            Company company = new Company();
            company.setCompanyCode(companyCode);
            company.setHrId(savedUser.getUserId());
            company.setCreatedDate(LocalDateTime.now().toString());
            companyRepository.save(company);

            return modelMapper.map(savedUser, UserDto.class);

        } else { // ROLE_USER
            if (user.getCompanyCode() != null) {
                Company company = companyRepository.findByCompanyCode(user.getCompanyCode())
                        .orElseThrow(() -> new ResourceNotFoundException("Company with this code not found"));

                // Save user WITHOUT setting companyCode yet
                user.setCompanyCode(null);
                User savedUser = userRepository.save(user);
                //put employee in waitlist of that company
                Set<String> waitListEmployee = company.getWaitListEmployeesId();
                if(waitListEmployee==null) waitListEmployee = new HashSet<>();
                waitListEmployee.add(savedUser.getUserId());
                companyRepository.save(company);
                return modelMapper.map(savedUser, UserDto.class);
            } else {
                User savedUser = userRepository.save(user);
                return modelMapper.map(savedUser, UserDto.class);
            }
        }
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        return null;
    }


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

}

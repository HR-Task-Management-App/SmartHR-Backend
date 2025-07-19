package com.hrms.backend.services.userService;

import com.hrms.backend.dtos.entityDtos.User.request.CreateUserRequestDto;
import com.hrms.backend.dtos.entityDtos.User.request.UserUpdateProfileRequestDto;
import com.hrms.backend.dtos.entityDtos.User.response.UserResponseDto;
import com.hrms.backend.dtos.response_message.SuccessApiResponseMessage;
import com.hrms.backend.models.Company;
import com.hrms.backend.models.User;
import com.hrms.backend.models.enums.Role;
import com.hrms.backend.exceptions.BadApiRequestException;
import com.hrms.backend.exceptions.ResourceNotFoundException;
import com.hrms.backend.repositories.CompanyRepository;
import com.hrms.backend.repositories.UserRepository;
import com.hrms.backend.services.superbaseImageStorageService.SuperbaseImageStorageServiceInterface;
import com.hrms.backend.utils.CodeGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashSet;
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
    private SuperbaseImageStorageServiceInterface superbaseImageStorageServiceInterface;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserResponseDto createUser(CreateUserRequestDto createUserRequestDto) {
        User user = modelMapper.map(createUserRequestDto, User.class);
        if (userRepository.findByEmail(createUserRequestDto.getEmail()).isPresent()) {
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
            company.setHr(savedUser.getId());
            company.setCreatedDate(LocalDateTime.now().toString());
            companyRepository.save(company);

            return modelMapper.map(savedUser, UserResponseDto.class);

        } else { // ROLE_USER
            if (user.getCompanyCode() != null) {
                Company company = companyRepository.findByCompanyCode(user.getCompanyCode())
                        .orElseThrow(() -> new ResourceNotFoundException("Company with this code not found"));

                // Save user WITHOUT setting companyCode yet
                user.setWaitingCompanyCode(user.getCompanyCode());
                user.setCompanyCode(null);
                User savedUser = userRepository.save(user);
                //put employee in waitlist of that company
                Set<String> waitListEmployee = company.getWaitListEmployees();
                if(waitListEmployee==null) waitListEmployee = new HashSet<>();
                waitListEmployee.add(savedUser.getId());
                companyRepository.save(company);
                return modelMapper.map(savedUser, UserResponseDto.class);
            } else {
                User savedUser = userRepository.save(user);
                return modelMapper.map(savedUser, UserResponseDto.class);
            }
        }
    }

    @Override
    public UserResponseDto updateUser(UserUpdateProfileRequestDto userUpdateProfileRequestDto, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Not able to update, try later!!"));
        modelMapper.map(userUpdateProfileRequestDto,user);
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserResponseDto.class);
    }

    //
        //getUserById
    @Override
    public UserResponseDto getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("Not able to fetch user profile, try later!!"));
        return modelMapper.map(user, UserResponseDto.class);
    }

    @Override
    public SuccessApiResponseMessage updateProfileImage(MultipartFile file, String userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("Not able to update user image, try later!!"));
        String url  = superbaseImageStorageServiceInterface.uploadImage(file,"profile-images");
        user.setImageUrl(url);
        modelMapper.map(userRepository.save(user), UserResponseDto.class);
        return new SuccessApiResponseMessage("User profile image updated successfully");
    }

    @Override
    public SuccessApiResponseMessage updateCompanyCode(String companyCode, String userId) {

        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("Not able to update company code, try later!!"));
        String waitingCompanyCode = user.getWaitingCompanyCode();
        String prevCompanyCode = user.getCompanyCode();
        Company toBeCompany = companyRepository.findByCompanyCode(companyCode).orElseThrow(() -> new ResourceNotFoundException("Company with this code does not exist!!"));


        if(companyCode.equals(prevCompanyCode) || companyCode.equals(waitingCompanyCode)){
            return new SuccessApiResponseMessage("Already applied to or in the same company");
        }

        if(waitingCompanyCode!=null){
            Company company = companyRepository.findByCompanyCode(waitingCompanyCode).orElseThrow(()-> new ResourceNotFoundException("Company code update failed, try later!!"));
            company.getWaitListEmployees().remove(userId);
            user.setWaitingCompanyCode(null);
            companyRepository.save(company);
        }

        if(prevCompanyCode==null || prevCompanyCode.isBlank()){ // prev it was empty/null
            if(companyCode.isBlank()){
                return new SuccessApiResponseMessage("Company code updated successfully");
            }
            else{
                user.setCompanyCode(null);
                user.setWaitingCompanyCode(toBeCompany.getCompanyCode());
                User savedUser = userRepository.save(user);
                //put employee in waitlist of that company
                toBeCompany.getWaitListEmployees().add(savedUser.getId());
                companyRepository.save(toBeCompany);
                return new SuccessApiResponseMessage("Once the HR of the company accept , company code will update");
            }
        }
        else{ // prev he/she was in a company
            Company prevCompany = companyRepository.findByCompanyCode(prevCompanyCode).orElseThrow(()-> new ResourceNotFoundException("Not able to update company code, try later!!"));
            //remove employee from prev company
            prevCompany.getEmployees().remove(userId);
            user.setCompanyCode(null);
            companyRepository.save(prevCompany);
            if(companyCode.isBlank()){
                return new SuccessApiResponseMessage("Company code updated successfully");
            }
            else{
                user.setCompanyCode(null);
                user.setWaitingCompanyCode(toBeCompany.getCompanyCode());
                User savedUser = userRepository.save(user);
                //put employee in waitlist of that company
                toBeCompany.getWaitListEmployees().add(savedUser.getId());
                companyRepository.save(toBeCompany);
                return new SuccessApiResponseMessage("Once the HR of the company accept , company code will update");
            }
        }

    }

    @Override
    public SuccessApiResponseMessage leaveCurrentCompany(String userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("Not able to update, try again later!!"));
        String companyCode = user.getCompanyCode();
        if(companyCode==null || companyCode.isBlank()){
            throw new BadApiRequestException("You are not employee of any company");
        }
        Company company = companyRepository.findByCompanyCode(companyCode).orElse(null);
        assert company != null;
        assert company.getEmployees() != null;
        company.getEmployees().remove(userId);
        user.setCompanyCode(null);
        companyRepository.save(company);
        userRepository.save(user);
        return new SuccessApiResponseMessage("Successfully leaved the company");
    }

    @Override
    public SuccessApiResponseMessage retrieveFromWaitlistCompany(String userId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("Not able to update, try again later!!"));
        String companyCode = user.getWaitingCompanyCode();
        if(companyCode==null || companyCode.isBlank()){
            throw new BadApiRequestException("You does not present in any company waitlist");
        }
        Company company = companyRepository.findByCompanyCode(companyCode).orElse(null);
        assert company != null;
        assert company.getWaitListEmployees()!=null;
        company.getWaitListEmployees().remove(userId);
        user.setWaitingCompanyCode(null);
        companyRepository.save(company);
        userRepository.save(user);
        return new SuccessApiResponseMessage("Successfully retrieved from waiting company");
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

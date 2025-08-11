package com.hrms.backend.controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.hrms.backend.dtos.entityDtos.LoginSignUp.GoogleLoginRequest;
import com.hrms.backend.dtos.entityDtos.LoginSignUp.GoogleSignUpRequest;
import com.hrms.backend.dtos.entityDtos.LoginSignUp.JwtRequest;
import com.hrms.backend.dtos.entityDtos.LoginSignUp.JwtResponse;
import com.hrms.backend.dtos.entityDtos.User.response.UserResponseDto;
import com.hrms.backend.exceptions.BadApiRequestException;
import com.hrms.backend.exceptions.ResourceNotFoundException;
import com.hrms.backend.models.Company;
import com.hrms.backend.models.User;
import com.hrms.backend.models.enums.Role;
import com.hrms.backend.repositories.CompanyRepository;
import com.hrms.backend.repositories.UserRepository;
import com.hrms.backend.security.JwtHelper;
import com.hrms.backend.utils.CodeGenerator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CompanyRepository companyRepository;

    @Value("${web.client.id}")
    private String webClientId;


    //method to generate token
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @RequestBody JwtRequest jwtRequest
    ) {
        this.doAuthenticate(jwtRequest.getEmail(), jwtRequest.getPassword());
        User user = (User) userDetailsService.loadUserByUsername(jwtRequest.getEmail());
        String token = jwtHelper.generateToken(user, user.getRole().name());
        JwtResponse jwtResponse = JwtResponse.builder().token(token).user(modelMapper.map(user, UserResponseDto.class)).build();
        return ResponseEntity.ok(jwtResponse);
    }


    private void doAuthenticate(String email, String password) {
        try {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
            authenticationManager.authenticate(authentication);
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Invalid Username or Password");
        }

    }

    @PostMapping("/googleLogin")
    public ResponseEntity<JwtResponse> googleLogin(@RequestBody GoogleLoginRequest request) {
        String idTokenString = request.getIdToken();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(webClientId)) // your Web Client ID
                .build();

        GoogleIdToken idToken;
        try {
            idToken = verifier.verify(idTokenString);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Invalid Google ID token");
        }

        if (idToken == null) {
            throw new ResourceNotFoundException("Invalid Google ID token");
        }

        GoogleIdToken.Payload payload = idToken.getPayload();
        String email = payload.getEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email not registered"));

        // Generate JWT
        String jwt = jwtHelper.generateToken(user, user.getRole().name());
        UserResponseDto userResponseDto = modelMapper.map(user, UserResponseDto.class);
        JwtResponse jwtResponse = JwtResponse.builder()
                .user(userResponseDto)
                .token(jwt)
                .build();

        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/googleSignUp")
    public ResponseEntity<JwtResponse> googleSignUp(@Valid @RequestBody GoogleSignUpRequest request) {
        String idTokenString = request.getIdToken();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(webClientId)) // Your Google Web Client ID
                .build();

        GoogleIdToken idToken;
        try {
            idToken = verifier.verify(idTokenString);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Invalid Google ID token");
        }

        if (idToken == null) {
            throw new ResourceNotFoundException("Invalid Google ID token");
        }

        GoogleIdToken.Payload payload = idToken.getPayload();
        String email = (String) payload.get("email");

        // Check if user already exists
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new BadApiRequestException("User already registered with this email");
        }

        // Create new User object
        User newUser = User.builder()
                .email(email)
                .name((String) payload.get("name"))
                .imageUrl((String) payload.get("picture"))
                .role(Role.valueOf(request.getRole())) // Either ROLE_HR or ROLE_USER
                .isGoogleUser(true)
                .createdAt(LocalDateTime.now().toString())
                .build();

        if (newUser.getRole().equals(Role.ROLE_HR)) {
            // Generate unique company code
            String companyCode = CodeGenerator.generateBase64Code();
            while (companyRepository.findByCompanyCode(companyCode).isPresent()) {
                companyCode = CodeGenerator.generateBase64Code();
            }
            newUser.setCompanyCode(companyCode);

            // Save HR user
            User savedUser = userRepository.save(newUser);

            // Create and save company
            Company company = Company.builder()
                    .companyCode(companyCode)
                    .hr(savedUser.getId())
                    .createdDate(LocalDateTime.now().toString())
                    .build();
            companyRepository.save(company);

            // Generate JWT
            String jwt = jwtHelper.generateToken(savedUser, savedUser.getRole().name());
            UserResponseDto userResponseDto = modelMapper.map(savedUser, UserResponseDto.class);
            JwtResponse jwtResponse = JwtResponse.builder()
                    .user(userResponseDto)
                    .token(jwt)
                    .build();
            return ResponseEntity.ok(jwtResponse);

        } else {
            // ROLE_USER – no company code provided
            User savedUser = userRepository.save(newUser);

            // Generate JWT
            String jwt = jwtHelper.generateToken(savedUser, savedUser.getRole().name());
            UserResponseDto userResponseDto = modelMapper.map(savedUser, UserResponseDto.class);
            JwtResponse jwtResponse = JwtResponse.builder()
                    .user(userResponseDto)
                    .token(jwt)
                    .build();
            return ResponseEntity.ok(jwtResponse);
        }
    }

}

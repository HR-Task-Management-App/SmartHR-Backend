package com.hrms.backend.User;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrms.backend.controllers.UserController;
import com.hrms.backend.dtos.entityDtos.User.request.UserUpdateProfileRequestDto;
import com.hrms.backend.dtos.entityDtos.User.response.UserResponseDto;
import com.hrms.backend.security.JwtHelper;
import com.hrms.backend.services.userService.UserServiceInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTests {

    private MockMvc mockMvc;
    private final UserServiceInterface userService = mock(UserServiceInterface.class);
    private final JwtHelper jwtHelper = mock(JwtHelper.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String token = "Bearer valid.jwt.token";

    @BeforeEach
    void setUp() {
        UserController userController = new UserController();
        ReflectionTestUtils.setField(userController, "userServiceInterface", userService);
        ReflectionTestUtils.setField(userController, "jwtHelper", jwtHelper);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }


    @Test
    void getUserProfile_shouldReturn200() throws Exception {
        String userId = "u123";
        UserResponseDto response = new UserResponseDto();
        response.setId(userId);
        response.setEmail("john@example.com");

        when(jwtHelper.getUserIdFromToken("valid.jwt.token")).thenReturn(userId);
        when(userService.getUserById(userId)).thenReturn(response);

        mockMvc.perform(get("/users")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId));
    }

    @Test
    void updateProfile_shouldReturn202() throws Exception {
        String userId = "u123";
        UserUpdateProfileRequestDto request = new UserUpdateProfileRequestDto();
        request.setName("Jane Doe");

        UserResponseDto response = new UserResponseDto();
        response.setId(userId);
        response.setName("Jane Doe");

        when(jwtHelper.getUserIdFromToken("valid.jwt.token")).thenReturn(userId);
        when(userService.updateUser(any(), eq(userId))).thenReturn(response);

        mockMvc.perform(patch("/users")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.name").value("Jane Doe"));
    }
}

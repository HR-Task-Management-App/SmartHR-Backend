package com.hrms.backend.configs;

import com.hrms.backend.dtos.entityDtos.Meeting.MeetingResponseInfo;
import com.hrms.backend.dtos.entityDtos.User.UserInfo;
import com.hrms.backend.exceptions.ResourceNotFoundException;
import com.hrms.backend.models.MeetingResponse;
import com.hrms.backend.models.User;
import com.hrms.backend.models.enums.LeaveStatus;
import com.hrms.backend.models.enums.LeaveType;
import com.hrms.backend.repositories.UserRepository;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Conditions;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class MyConfig {

    @Autowired
    private UserRepository userRepository;

    @Bean
    public ModelMapper mapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Skip only nulls, but allow blank strings (like "")
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());

        // String to LocalDate
        modelMapper.addConverter(new AbstractConverter<String, LocalDate>() {
            @Override
            protected LocalDate convert(String source) {
                return source != null && !source.isBlank() ? LocalDate.parse(source) : null;
            }
        });

        // String to LocalDateTime
        modelMapper.addConverter(new AbstractConverter<String, LocalDateTime>() {
            @Override
            protected LocalDateTime convert(String source) {
                return source != null && !source.isBlank() ? LocalDateTime.parse(source) : null;
            }
        });

        // String to LeaveType (Enum)
        modelMapper.addConverter(new AbstractConverter<String, LeaveType>() {
            @Override
            protected LeaveType convert(String source) {
                return source != null && !source.isBlank() ? LeaveType.valueOf(source.toUpperCase()) : null;
            }
        });

        // String to LeaveStatus (Enum)
        modelMapper.addConverter(new AbstractConverter<String, LeaveStatus>() {
            @Override
            protected LeaveStatus convert(String source) {
                return source != null && !source.isBlank() ? LeaveStatus.valueOf(source.toUpperCase()) : null;
            }
        });

        modelMapper.addConverter(new AbstractConverter<String, UserInfo>() {
            @Override
            protected UserInfo convert(String source) {
                User userById = userRepository.findById(source).orElse(null);
                if(userById!=null) return UserInfo.builder().name(userById.getName()).id(userById.getId()).email(userById.getEmail()).imageUrl(userById.getImageUrl()).build(); else return null;
            }
        });

        modelMapper.addConverter(new AbstractConverter<Set<String>, List<UserInfo>>() {
            @Override
            protected List<UserInfo> convert(Set<String> source) {
                List<User> users = userRepository.findAllByIdIn(source);
                return users.stream().map(userById-> UserInfo.builder().name(userById.getName()).id(userById.getId()).email(userById.getEmail()).imageUrl(userById.getImageUrl()).build()).collect(Collectors.toList());
            }
        });

        Converter<MeetingResponse, MeetingResponseInfo> meetingResponseToInfoConverter = ctx -> {
            MeetingResponse source = ctx.getSource();
            User user = userRepository.findById(source.getParticipant())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            UserInfo userInfo = new UserInfo(user.getId(), user.getName(), user.getEmail(), user.getImageUrl());
            return new MeetingResponseInfo(userInfo, source.getStatus());
        };
        // Register the converter
        modelMapper.createTypeMap(MeetingResponse.class, MeetingResponseInfo.class)
                .setConverter(meetingResponseToInfoConverter);


        // String → Double converter
        modelMapper.addConverter(new AbstractConverter<String, Double>() {
            @Override
            protected Double convert(String source) {
                try {
                    return source != null ? Double.parseDouble(source) : null;
                } catch (NumberFormatException e) {
                    return null; // or handle exception as needed
                }
            }
        });

// Double → String converter
        modelMapper.addConverter(new AbstractConverter<Double, String>() {
            @Override
            protected String convert(Double source) {
                return source != null ? source.toString() : null;
            }
        });

        //String to Instant
        modelMapper.addConverter(new AbstractConverter<String, Instant>() {
            @Override
            protected Instant convert(String source) {
                return source != null ? Instant.parse(source) : null;
            }
        });

        //Instant to String
        modelMapper.addConverter(new AbstractConverter<Instant, String>() {
            @Override
            protected String convert(Instant source) {
                return source != null ? source.toString() : null;
            }
        });





        return modelMapper;
    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}

package com.hrms.backend.services.attendanceService;

import com.hrms.backend.dtos.entityDtos.Attendance.AttendanceRequestDto;
import com.hrms.backend.dtos.entityDtos.Attendance.AttendanceResponseDto;
import com.hrms.backend.exceptions.BadApiRequestException;
import com.hrms.backend.exceptions.ResourceNotFoundException;
import com.hrms.backend.models.Attendance;
import com.hrms.backend.models.Company;
import com.hrms.backend.models.User;
import com.hrms.backend.models.enums.AttendanceType;
import com.hrms.backend.repositories.AttendanceRepository;
import com.hrms.backend.repositories.CompanyRepository;
import com.hrms.backend.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttendanceServiceImpl implements AttendanceServiceInterface{

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;


    //By employee
    public AttendanceResponseDto markEmployeeAttendance(
            String userId,
            AttendanceRequestDto attendanceRequestDto
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User is not valid!!"));
        String companyCode = user.getCompanyCode();
        Company company = companyRepository.findByCompanyCode(companyCode)
                .orElseThrow(() -> new ResourceNotFoundException("Not a valid company!!"));

        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        Instant startOfDay = today.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant endOfDay = today.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant().minusNanos(1);

        Attendance existingAttendance = attendanceRepository
                .findByEmployeeAndCheckInBetween(userId, startOfDay, endOfDay)
                .orElse(null);

        Instant now = Instant.now();

        if (now.isBefore(startOfDay) || now.isAfter(endOfDay)) {
            throw new BadApiRequestException("Attendance can only be marked for today.");
        }

        if (existingAttendance != null) {
            // UPDATE CASE
            if (attendanceRequestDto.type.equals("CHECKIN")) {
                if (existingAttendance.getCheckIn() != null) {
                    throw new BadApiRequestException("Employee has already checked in today");
                }
                existingAttendance.setCheckIn(now);
            } else if (attendanceRequestDto.type.equals("CHECKOUT")) {
                if (existingAttendance.getCheckOut() != null) {
                    throw new BadApiRequestException("Employee has already checked out today");
                }
                if (existingAttendance.getCheckIn() == null) {
                    throw new BadApiRequestException("Employee should first check-in");
                }
                existingAttendance.setCheckOut(now);
            }

            existingAttendance.setLatitude(attendanceRequestDto.latitude);
            existingAttendance.setLongitude(attendanceRequestDto.longitude);
            existingAttendance.setType(AttendanceType.valueOf(attendanceRequestDto.type));

            return mapper.map(attendanceRepository.save(existingAttendance), AttendanceResponseDto.class);
        } else {
            // CREATE CASE (Only for CHECKIN)
            if (attendanceRequestDto.type.equals("CHECKOUT")) {
                throw new BadApiRequestException("Employee should first check-in");
            }

            Attendance newAttendance = Attendance.builder()
                    .employee(userId)
                    .companyCode(companyCode)
                    .type(AttendanceType.valueOf(attendanceRequestDto.type))
                    .latitude(attendanceRequestDto.latitude)
                    .longitude(attendanceRequestDto.longitude)
                    .checkIn(now)
                    .build();

            return mapper.map(attendanceRepository.save(newAttendance), AttendanceResponseDto.class);
        }
    }



    //employee
    public List<AttendanceResponseDto> getEmployeeAttendanceHistory(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User is not valid!!"));

        String companyCode = user.getCompanyCode();
        companyRepository.findByCompanyCode(companyCode)
                .orElseThrow(() -> new ResourceNotFoundException("Not a valid company!!"));

        List<Attendance> attendancesOfEmployee = attendanceRepository.findByEmployee(userId);

        // Sort by latest activity (checkIn/checkOut)
        attendancesOfEmployee.sort((a1, a2) -> {
            Instant a1Latest = latestOf(a1.getCheckIn(), a1.getCheckOut());
            Instant a2Latest = latestOf(a2.getCheckIn(), a2.getCheckOut());
            return a2Latest.compareTo(a1Latest); // Descending
        });

        // Convert to DTO using mapper
        return attendancesOfEmployee.stream()
                .map(attendance -> mapper.map(attendance, AttendanceResponseDto.class))
                .collect(Collectors.toList());
    }



    //HR
    public List<AttendanceResponseDto> getCompanyAttendanceByDate(String hrId, Optional<LocalDate> optionalDate) {
        User user = userRepository.findById(hrId)
                .orElseThrow(() -> new ResourceNotFoundException("User is not valid!!"));

        String companyCode = user.getCompanyCode();

        companyRepository.findByCompanyCode(companyCode)
                .orElseThrow(() -> new ResourceNotFoundException("Company is not valid!!"));

        // Use today's date if none provided
        LocalDate date = optionalDate.orElse(LocalDate.now(ZoneOffset.UTC));
        Instant startOfDay = date.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant endOfDay = date.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant().minusNanos(1);

        List<Attendance> attendances = attendanceRepository
                .findByCompanyCodeAndCheckInBetween(companyCode, startOfDay, endOfDay);

        return attendances.stream()
                .map(attendance -> mapper.map(attendance, AttendanceResponseDto.class))
                .collect(Collectors.toList());
    }





    private Instant latestOf(Instant i1, Instant i2) {
        if (i1 == null && i2 == null) return Instant.MIN;
        if (i1 == null) return i2;
        if (i2 == null) return i1;
        return i1.isAfter(i2) ? i1 : i2;
    }
}



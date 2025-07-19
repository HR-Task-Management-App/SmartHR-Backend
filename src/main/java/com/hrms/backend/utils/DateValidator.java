package com.hrms.backend.utils;

import com.hrms.backend.exceptions.BadApiRequestException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class DateValidator {

    public static void startEndDateValidator(String startDate, String endDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate today = LocalDate.now();
        LocalDate localStartDate;
        LocalDate localEndDate;

        try {
            localStartDate = LocalDate.parse(startDate, formatter);
            localEndDate = LocalDate.parse(endDate, formatter);
        } catch (DateTimeParseException e) {
            throw new BadApiRequestException("Invalid date format. Use yyyy-MM-dd.");
        }

// 1. Check if startDate is before today
        if (localStartDate.isBefore(today)) {
            throw new BadApiRequestException("Start date cannot be in the past.");
        }

// 2. Check if startDate is after endDate
        if (localStartDate.isAfter(localEndDate)) {
            throw new BadApiRequestException("Start date must be before or same as end date.");
        }

// Optional: Check if leave range is too long (e.g., > 30 days)
        long daysBetween = ChronoUnit.DAYS.between(localStartDate, localEndDate);
        if (daysBetween > 30) {
            throw new BadApiRequestException("Leave duration cannot exceed 30 days.");
        }

    }
}

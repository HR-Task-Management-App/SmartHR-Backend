package com.hrms.backend.Validations.validators;

import com.hrms.backend.Validations.annotations.ValidDateTimeFormat;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeFormatValidator implements ConstraintValidator<ValidDateTimeFormat, String> {
    private static final String PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) return false;
        try {
            LocalDateTime.parse(value, DateTimeFormatter.ofPattern(PATTERN));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}

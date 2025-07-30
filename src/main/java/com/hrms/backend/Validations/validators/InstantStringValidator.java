package com.hrms.backend.Validations.validators;

import com.hrms.backend.Validations.annotations.ValidInstant;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Instant;
import java.time.format.DateTimeParseException;

public class InstantStringValidator implements ConstraintValidator<ValidInstant, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) return true; // use @NotBlank if required

        try {
            Instant.parse(value);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}

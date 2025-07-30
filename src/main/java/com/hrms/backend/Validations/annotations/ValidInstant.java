package com.hrms.backend.Validations.annotations;

import com.hrms.backend.Validations.validators.InstantStringValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = InstantStringValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidInstant {
    String message() default "must be a valid ISO-8601 Instant string (e.g., 2024-07-25T12:00:00Z)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

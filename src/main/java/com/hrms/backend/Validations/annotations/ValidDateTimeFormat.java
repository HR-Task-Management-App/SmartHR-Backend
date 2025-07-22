package com.hrms.backend.Validations.annotations;

import com.hrms.backend.Validations.validators.DateTimeFormatValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateTimeFormatValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDateTimeFormat {
    String message() default "Invalid date-time format. Expected: yyyy-MM-dd'T'HH:mm:ss";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

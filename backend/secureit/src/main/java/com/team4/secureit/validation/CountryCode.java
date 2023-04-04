package com.team4.secureit.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = CountryCodeValidator.class)
@Documented
public @interface CountryCode {
    String message() default "Field must be equal to ISO 3116 country code";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

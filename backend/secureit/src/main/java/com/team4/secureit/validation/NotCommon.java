package com.team4.secureit.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD, TYPE_USE })
@Retention(RUNTIME)
@Constraint(validatedBy = NotCommonValidator.class)
@Documented
public @interface NotCommon {
    String message() default "Password is in the list of common passwords.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

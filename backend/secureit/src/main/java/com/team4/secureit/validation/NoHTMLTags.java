package com.team4.secureit.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD, TYPE_USE })
@Retention(RUNTIME)
@Constraint(validatedBy = NoHTMLTagsValidator.class)
@Documented
public @interface NoHTMLTags {
    String message() default "Invalid input.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

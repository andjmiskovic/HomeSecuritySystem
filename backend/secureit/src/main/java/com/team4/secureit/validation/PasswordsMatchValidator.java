package com.team4.secureit.validation;

import com.team4.secureit.dto.request.RegistrationRequest;
import com.team4.secureit.util.PasswordChecker;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, RegistrationRequest> {

    private PasswordChecker commonPasswordChecker;

    @Override
    public void initialize(PasswordsMatch constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.commonPasswordChecker = new PasswordChecker();
    }

    @Override
    public boolean isValid(RegistrationRequest request, ConstraintValidatorContext constraintValidatorContext) {
        return passwordsMatch(request.getPassword(), request.getPasswordConfirmation())
                && commonPasswordChecker.isCommonPassword(request.getPassword());
    }

    private Boolean passwordsMatch(String password, String confirmation) {
        return password != null && password.equals(confirmation);
    }
}

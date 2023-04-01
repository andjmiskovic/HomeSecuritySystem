package com.team4.secureit.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.Locale;

public class CountryCodeValidator implements ConstraintValidator<CountryCode, String> {
    private List<String> countryCodes;

    @Override
    public void initialize(CountryCode constraintAnnotation) {
        countryCodes = List.of(Locale.getISOCountries());
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s.length() != 2)
            return false;

        return countryCodes.contains(s.toUpperCase());
    }
}

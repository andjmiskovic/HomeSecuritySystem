package com.team4.secureit.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

/*
This validator will clean up any HTML and script tags, and check if the original input was changed.
 */
public class NoHTMLTagsValidator implements ConstraintValidator<NoHTMLTags, String> {

    @Override
    public void initialize(NoHTMLTags constraintAnnotation) {

    }

    @Override
    public boolean isValid(String userInput, ConstraintValidatorContext constraintValidatorContext) {
        String escaped = Jsoup.clean(userInput, Safelist.none());
        return userInput.equals(escaped);
    }
}

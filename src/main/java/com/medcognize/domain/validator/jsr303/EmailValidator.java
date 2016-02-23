package com.medcognize.domain.validator.jsr303;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<Email, String> {

    private static final String EMAIL_REGEX =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern PATTERN = Pattern.compile(EMAIL_REGEX);

    @Override
    public void initialize(Email email) {
    }

    @Override
    public boolean isValid(String candidate, ConstraintValidatorContext context) {
        if (null == candidate) {
            return false;
        }
        boolean b = PATTERN.matcher(candidate).matches();
        return b;
    }

}
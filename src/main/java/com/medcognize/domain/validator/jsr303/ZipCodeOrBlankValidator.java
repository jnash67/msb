package com.medcognize.domain.validator.jsr303;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ZipCodeOrBlankValidator implements ConstraintValidator<ZipCodeOrBlank, String> {

    @Override
    public void initialize(ZipCodeOrBlank zipCode) {
    }

    @Override
    public boolean isValid(String string, ConstraintValidatorContext context) {
        if (null == string) {
            return true;
        }
        if (0 == string.length()) {
            return true;
        }
        if (string.length() != 5)
            return false;
        for (char c : string.toCharArray()) {
            if (!Character.isDigit(c))
                return false;
        }
        return true;
    }

}
package com.medcognize.domain.validator.vaadin;

import com.vaadin.data.validator.AbstractValidator;

//
// Validator for validating the passwords
//
public class PasswordRequirementsValidator extends AbstractValidator<String> {

    public PasswordRequirementsValidator() {
        super("The password must be at least 8 characters long and contain at least one number");
    }

    @Override
    protected boolean isValidValue(String password) {
        //
        // Password must be at least 8 characters long and contain at least
        // one number
        //
        if (password != null && (password.length() < 8 || !password.matches(".*\\d.*"))) {
            return false;
        }
        return true;
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }
}
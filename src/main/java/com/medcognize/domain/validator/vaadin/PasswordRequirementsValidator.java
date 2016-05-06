package com.medcognize.domain.validator.vaadin;

import com.medcognize.util.Password;
import com.vaadin.data.validator.AbstractValidator;

//
// Validator for validating the passwords
//
public class PasswordRequirementsValidator extends AbstractValidator<Password> {

    public PasswordRequirementsValidator() {
        super("The password must be at least 8 characters long and contain at least one number");
    }

    @Override
    protected boolean isValidValue(Password password) {
        //
        // Password must be at least 8 characters long and contain at least
        // one number
        //
        String value = password.getValue();
        if (value != null && (value.length() < 8 || !value.matches(".*\\d.*"))) {
            return false;
        }
        return true;
    }

    @Override
    public Class<Password> getType() {
        return Password.class;
    }
}
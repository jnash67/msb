package com.medcognize.domain.validator.vaadin;

import com.medcognize.UserRepository;
import com.medcognize.util.DbUtil;
import com.vaadin.data.validator.AbstractValidator;

public class ExistingUsernameValidator extends AbstractValidator<String> {

    // when editing, we don't want to invalidate the user submitting the
    // previously existing value
    private final String optionalExcept;
    private final UserRepository repo;

    public ExistingUsernameValidator(UserRepository repo, String optionalExcept) {
        super("An account already exists with that email address");
        this.repo = repo;
        this.optionalExcept = optionalExcept;
    }

    @Override
    protected boolean isValidValue(String value) {
        if (null != optionalExcept) {
            if (optionalExcept.equals(value)) {
                return true;
            }
        }
        return !DbUtil.existsUser(repo, value);
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }
}

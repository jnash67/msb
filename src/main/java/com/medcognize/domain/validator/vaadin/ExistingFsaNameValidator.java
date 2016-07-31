package com.medcognize.domain.validator.vaadin;

import com.medcognize.domain.Fsa;
import com.medcognize.util.DbUtil;
import com.medcognize.util.UserUtil;
import com.vaadin.data.validator.AbstractValidator;

import java.util.Collection;

public class ExistingFsaNameValidator extends AbstractValidator<String> {

    // when editing, we don't want to invalidate the user submitting the
    // previously existing value
    private final String optionalExcept;

    public ExistingFsaNameValidator(String optionalExcept) {
        super("That name already exists");
        this.optionalExcept = optionalExcept;
    }

    @Override
    protected boolean isValidValue(String value) {
        // this condition will be caught by bean validation annotation
        if (null == value) {
            return true;
        }
        if ("".equals(value)) {
            return true;
        }
        if (null != optionalExcept) {
            if (optionalExcept.equals(value)) {
                return true;
            }
        }
        Collection<Fsa> fsas = UserUtil.getAllFromUser(DbUtil.getLoggedInUser(), Fsa.class);
        for (Fsa f : fsas) {
            if (value.equals(f.getFsaName())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }
}
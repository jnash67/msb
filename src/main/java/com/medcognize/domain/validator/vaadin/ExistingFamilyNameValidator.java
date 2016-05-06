package com.medcognize.domain.validator.vaadin;

import com.medcognize.domain.FamilyMember;
import com.medcognize.util.DbUtil;
import com.vaadin.data.validator.AbstractValidator;

import java.util.Collection;

public class ExistingFamilyNameValidator extends AbstractValidator<String> {

    // when editing, we don't want to invalidate the user submitting the
    // previously existing value
    private final String optionalExcept;

    public ExistingFamilyNameValidator(String optionalExcept) {
        super("That name already exists");
        this.optionalExcept = optionalExcept;
    }

    @Override
    protected boolean isValidValue(String value) {
        // this condition will be caught by bean validation annotation
        if ("".equals(value)) {
            return true;
        }
        if (null != optionalExcept) {
            if (optionalExcept.equals(value)) {
                return true;
            }
        }
        Collection<FamilyMember> fms = DbUtil.getLoggedInUser().getAll(FamilyMember.class);
        for (FamilyMember fm : fms) {
            if (value.equals(fm.getFamilyMemberName())) {
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

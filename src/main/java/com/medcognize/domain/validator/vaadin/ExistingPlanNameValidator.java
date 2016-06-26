package com.medcognize.domain.validator.vaadin;

import com.medcognize.domain.Plan;
import com.medcognize.util.DbUtil;
import com.medcognize.util.UserUtil;
import com.vaadin.data.validator.AbstractValidator;

import java.util.Collection;

public class ExistingPlanNameValidator extends AbstractValidator<String> {

    // when editing, we don't want to invalidate the user submitting the
    // previously existing value
    private final String optionalExcept;

    public ExistingPlanNameValidator(String optionalExcept) {
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
        Collection<Plan> pls = UserUtil.getAll(DbUtil.getLoggedInUser(), Plan.class);
        for (Plan p: pls) {
            if (value.equals(p.getPlanName())) {
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

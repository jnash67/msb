package com.medcognize.domain.validator.vaadin;

import com.medcognize.domain.Provider;
import com.medcognize.util.DbUtil;
import com.vaadin.data.validator.AbstractValidator;

import java.util.Collection;

public class ExistingProviderNameValidator extends AbstractValidator<String> {

    // when editing, we don't want to invalidate the user submitting the
    // previously existing value
    private final String optionalExcept;

    public ExistingProviderNameValidator(String optionalExcept) {
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
        Collection<Provider> ps = DbUtil.getLoggedInUser().getAll(Provider.class);
        for (Provider p : ps) {
            if (value.equals(p.getProviderName())) {
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

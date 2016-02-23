package com.medcognize.domain.validator.vaadin;

import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.ui.Field;

public class NotEqualIntegerValidator extends AbstractValidator<Integer> {
    boolean compareToField;
    Field otherField;
    Integer constantValue;

    public NotEqualIntegerValidator(String message, Field<?> otherIntegerFieldWeMustBeNotEqualTo) {
        super(message);
        this.otherField = otherIntegerFieldWeMustBeNotEqualTo;
        this.compareToField = true;
    }

    public NotEqualIntegerValidator(String message, Integer constantValueWeMustBeNotEqualTo) {
        super(message);
        this.constantValue = constantValueWeMustBeNotEqualTo;
        this.compareToField = false;
    }

    @Override
    protected boolean isValidValue(Integer value) {
        boolean b;
        if (compareToField) {
            b = !value.equals(otherField.getValue());
        } else {
            b = !value.equals(constantValue);
        }
        return b;
    }

    @Override
    public Class<Integer> getType() {
        return Integer.class;
    }
}
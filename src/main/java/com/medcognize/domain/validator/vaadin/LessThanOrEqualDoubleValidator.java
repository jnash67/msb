package com.medcognize.domain.validator.vaadin;

import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.ui.Field;

public class LessThanOrEqualDoubleValidator extends AbstractValidator<Double> {
    boolean compareToField;
    boolean orEqualTo;
    Field otherField;
    Double constantValue;

    public LessThanOrEqualDoubleValidator(String message, Field<?> otherDoubleFieldWeMustBeLessThan, boolean orEqualTo) {
        super(message);
        this.otherField = otherDoubleFieldWeMustBeLessThan;
        this.compareToField = true;
        this.orEqualTo = orEqualTo;
    }

    public LessThanOrEqualDoubleValidator(String message, Double constantValueWeMustBeLessThan, boolean orEqualTo) {
        super(message);
        this.constantValue = constantValueWeMustBeLessThan;
        this.compareToField = false;
        this.orEqualTo = orEqualTo;
    }

    @Override
    protected boolean isValidValue(Double value) {
        if (compareToField) {
            if (orEqualTo) {
                return value <= (Double) otherField.getValue();
            }   else {
                return value < (Double) otherField.getValue();
            }
        } else {
            if (orEqualTo) {
                return value <= constantValue;
            }   else {
                return value < constantValue;
            }
        }
    }

    @Override
    public Class<Double> getType() {
        return Double.class;
    }
}
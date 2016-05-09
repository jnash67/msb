package com.medcognize.domain.validator.jsr303;

import org.joda.time.DateTime;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.PropertyAccessorFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Date;

// from: https://stackoverflow.com/questions/1972933/cross-field-validation-with-hibernate-validator-jsr-303
public class DateFieldInYearValidator implements ConstraintValidator<DateFieldInYear, Object> {
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(final DateFieldInYear constraintAnnotation) {
        firstFieldName = constraintAnnotation.dateField();
        secondFieldName = constraintAnnotation.yearField();
    }

    @Override
    public boolean isValid(final Object bean, final ConstraintValidatorContext context) {
        try {
            // see also BeanWrapperImpl
            final Object firstObj = PropertyAccessorFactory.forBeanPropertyAccess(bean).getPropertyValue(firstFieldName);
            final Object secondObj = PropertyAccessorFactory.forBeanPropertyAccess(bean).getPropertyValue(secondFieldName);
            if ((null == firstObj) || (null == secondObj)) {
                return false;
            }
            int y;
            //noinspection ConstantConditions
            if (secondObj instanceof Integer) {
                y = (Integer) secondObj;
            } else {
                if (secondObj.getClass().isAssignableFrom(int.class)) {
                    y = (int) secondObj;
                } else {
                    return false;
                }
            }
            //noinspection ConstantConditions
            if (firstObj instanceof Date) {
                DateTime dt = new DateTime(firstObj);
                if (dt.getYear() == y) {
                    return true;
                }
            }
        } catch (final Exception ignore) {
            // ignore
        }
        return false;
    }
}
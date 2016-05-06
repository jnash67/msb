package com.medcognize.domain.validator.jsr303;

import org.apache.commons.beanutils.BeanUtils;
import org.joda.time.DateTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Date;

// from: https://stackoverflow.com/questions/1972933/cross-field-validation-with-hibernate-validator-jsr-303
public class DateFieldOnOrAfterValidator implements ConstraintValidator<DateFieldOnOrAfter, Object> {
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(final DateFieldOnOrAfter constraintAnnotation) {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(final Object bean, final ConstraintValidatorContext context) {
        try {
            final Object firstObj = BeanUtils.getProperty(bean, firstFieldName);
            final Object secondObj = BeanUtils.getProperty(bean, secondFieldName);
            if ((null == firstObj) || (null == secondObj)) {
                return false;
            }
	        //noinspection ConstantConditions
	        if ((firstObj instanceof Date) && (secondObj instanceof Date)) {
                DateTime f = new DateTime(firstObj);
                DateTime s = new DateTime(secondObj);
                if ((f.withTimeAtStartOfDay().isEqual(s.withTimeAtStartOfDay())) || (f.withTimeAtStartOfDay().isAfter
                        (s.withTimeAtStartOfDay()))) {
                    return true;
                }
            }
        } catch (final Exception ignore) {
            // ignore
        }
        return false;
    }
}
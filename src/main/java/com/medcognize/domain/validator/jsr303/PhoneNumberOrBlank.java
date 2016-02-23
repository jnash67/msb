package com.medcognize.domain.validator.jsr303;

import javax.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PhoneNumberOrBlankValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@SuppressWarnings("UnusedDeclaration")
public @interface PhoneNumberOrBlank {
    String message() default "That's not a real phone number!";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
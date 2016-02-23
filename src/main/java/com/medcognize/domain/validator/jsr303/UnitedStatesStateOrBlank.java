package com.medcognize.domain.validator.jsr303;

import javax.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = UnitedStatesStateOrBlankValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@SuppressWarnings("UnusedDeclaration")
public @interface UnitedStatesStateOrBlank {
    String message() default "That's not a real state!";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
package com.medcognize.domain.validator.jsr303;

import javax.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = ZipCodeOrBlankValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ZipCodeOrBlank {
    String message() default "zip code must be five numeric characters";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
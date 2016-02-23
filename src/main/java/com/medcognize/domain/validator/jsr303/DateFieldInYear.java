package com.medcognize.domain.validator.jsr303;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = DateFieldInYearValidator.class)
@Documented
@SuppressWarnings("UnusedDeclaration")
public @interface DateFieldInYear
{
    String message() default "Date is not in correct year";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * @return The first date field
     */
    String dateField();

    /**
     * @return The second date field
     */
    String yearField();

    /**
     * Defines several <code>@DateFieldInYear</code> annotations on the same element
     *
     * @see DateFieldInYear
     */
    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List
    {
        DateFieldInYear[] value();
    }
}

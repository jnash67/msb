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
@Constraint(validatedBy = DateFieldOnOrAfterValidator.class)
@Documented
@SuppressWarnings("UnusedDeclaration")
public @interface DateFieldOnOrAfter
{
    String message() default "First date must be on or after the second";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * @return The first date field
     */
    String first();

    /**
     * @return The second date field
     */
    String second();

    /**
     * Defines several <code>@DateFieldOnOrAfter</code> annotations on the same element
     *
     * @see DateFieldOnOrAfter
     */
    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List
    {
        DateFieldOnOrAfter[] value();
    }
}

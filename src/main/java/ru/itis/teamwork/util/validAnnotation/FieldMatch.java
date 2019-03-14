package ru.itis.teamwork.util.validAnnotation;

import ru.itis.teamwork.util.validator.FieldMatchValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FieldMatchValidator.class)
@Documented
public @interface FieldMatch {
    public String message() default "{repeatPassword.match}";

    public String firstField();
    public String secondField();

    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        FieldMatch[] value();
    }

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

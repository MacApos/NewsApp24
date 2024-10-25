package validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import validator.impl.StateValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = StateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface State {
    String message() default "Insert valid state.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

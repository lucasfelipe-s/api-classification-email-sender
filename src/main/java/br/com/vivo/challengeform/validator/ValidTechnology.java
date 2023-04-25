package br.com.vivo.challengeform.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

@Target({ElementType.TYPE, ElementType.PARAMETER, METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@jakarta.validation.Constraint(validatedBy = TechnologyValidator.class)
@Documented
public @interface ValidTechnology {
	String message() default "Tecnologia inválida";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

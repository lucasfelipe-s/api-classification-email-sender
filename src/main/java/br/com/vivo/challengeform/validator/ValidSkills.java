package br.com.vivo.challengeform.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

@Target({ElementType.PARAMETER, METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@jakarta.validation.Constraint(validatedBy = SkillValidator.class)
@Documented
public @interface ValidSkills {
	String message() default "Habilidade inválida";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

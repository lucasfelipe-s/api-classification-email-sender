package br.com.vivo.challengeform.validator;

import br.com.vivo.challengeform.dto.LevelDTO;
import br.com.vivo.challengeform.exceptions.resources.ResourceBadRequestException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LevelValidator implements ConstraintValidator<ValidLevel, LevelDTO> {
    @Override
    public boolean isValid(LevelDTO level, ConstraintValidatorContext context) {
        int levelValue = level.getValue();

        if(levelValue < 0 || levelValue > 10) {
            throw new ResourceBadRequestException("O nível da habilidade '"
                    +level.getTechnology().getName()+ "' deve ser um número entre 0 e 10");
        }

        return true;
    }
}

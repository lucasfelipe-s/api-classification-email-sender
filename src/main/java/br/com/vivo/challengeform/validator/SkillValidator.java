package br.com.vivo.challengeform.validator;

import java.util.ArrayList;
import java.util.List;

import br.com.vivo.challengeform.dto.SkillDTO;
import br.com.vivo.challengeform.exceptions.resources.ResourceBadRequestException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SkillValidator implements ConstraintValidator<ValidSkills, List<SkillDTO>> {

	@Override
	public boolean isValid(List<SkillDTO> skills, ConstraintValidatorContext context) {

		if(skills == null ) {
			throw new ResourceBadRequestException("A lista de habilidades não pode ser nula");
		}

		if(skills.size() == 0) throw new ResourceBadRequestException("A lista de habilidades não pode ser vazia");

		List<String> availableSkills = new ArrayList<>();
		availableSkills.add("JAVA");
		availableSkills.add("C#");
		availableSkills.add("Golang");
		availableSkills.add("JavaScript");
		availableSkills.add("VUE");
		availableSkills.add("PHP");

		for (SkillDTO skill : skills) {
			boolean nameSkillValid = false;
			String name = skill.getName();

			if (name == null || name.isBlank()) {
				throw new ResourceBadRequestException("O nome da habilidade não pode ser vazio");
			}

			int rating = skill.getRating();
			if(rating < 0 || rating > 10) {
				throw new ResourceBadRequestException("O nível da habilidade '" +name+ "' deve ser um número entre 0 e 10");
			}

	        for (String availableSkill : availableSkills) {
	        	if(name.equalsIgnoreCase(availableSkill)) {
	        		nameSkillValid = true;
	        		break;
	        	}
	        }

	        if(!nameSkillValid) {
				throw new ResourceBadRequestException("O nome da habilidade '" + name+ "' está incorreto");
	        }
		}

		return true;
	}
}

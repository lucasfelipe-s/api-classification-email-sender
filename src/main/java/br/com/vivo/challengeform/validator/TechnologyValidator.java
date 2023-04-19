package br.com.vivo.challengeform.validator;

import java.util.ArrayList;
import java.util.List;

import br.com.vivo.challengeform.dto.TechnologyDTO;
import br.com.vivo.challengeform.exceptions.resources.ResourceBadRequestException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TechnologyValidator implements ConstraintValidator<ValidTechnologies, List<TechnologyDTO>> {

	@Override
	public boolean isValid(List<TechnologyDTO> technologies, ConstraintValidatorContext context) {

		if(technologies == null ) {
			throw new ResourceBadRequestException("A lista de habilidades não pode ser nula");
		}

		if(technologies.size() == 0) throw new ResourceBadRequestException("A lista de habilidades não pode ser vazia");

		List<String> availableTechnologies = new ArrayList<>();
		availableTechnologies.add("JAVA");
		availableTechnologies.add("C#");
		availableTechnologies.add("Golang");
		availableTechnologies.add("JavaScript");
		availableTechnologies.add("VUE");
		availableTechnologies.add("PHP");

		for (TechnologyDTO technology : technologies) {
			boolean nameTechnologyIsValid = false;
			String technologyName = technology.getName();

			if (technologyName == null || technologyName.isBlank()) {
				throw new ResourceBadRequestException("O nome da tecnologia não pode ser vazio");
			}

			int rating = technology.getRating();
			if(rating < 0 || rating > 10) {
				throw new ResourceBadRequestException("O nível da tecnologia '" +technologyName+ "' deve ser um número entre 0 e 10");
			}

	        for (String availableTechnology : availableTechnologies) {
	        	if(technologyName.equalsIgnoreCase(availableTechnology)) {
	        		nameTechnologyIsValid = true;
	        		break;
	        	}
	        }

	        if(!nameTechnologyIsValid) {
				throw new ResourceBadRequestException("O nome da habilidade '" + technologyName+ "' está incorreto");
	        }
		}

		return true;
	}
}

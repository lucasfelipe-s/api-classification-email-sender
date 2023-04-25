package br.com.vivo.challengeform.validator;

import java.util.List;

import br.com.vivo.challengeform.dto.TechnologyDTO;
import br.com.vivo.challengeform.enums.TechnologyTypes;
import br.com.vivo.challengeform.entities.Technology;
import br.com.vivo.challengeform.exceptions.resources.ResourceBadRequestException;
import br.com.vivo.challengeform.repository.TechnologyRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TechnologyValidator implements ConstraintValidator<ValidTechnology, TechnologyDTO> {

	private final TechnologyRepository technologyRepository;

	public TechnologyValidator(TechnologyRepository technologyRepository) {
		this.technologyRepository = technologyRepository;
	}

	@Override
	public boolean isValid(TechnologyDTO technology, ConstraintValidatorContext context) {

		String technologyName = technology.getName();
		TechnologyTypes technologyType = technology.getType();

		if (technologyName == null || technologyName.isBlank()) {
			throw new ResourceBadRequestException("O nome da tecnologia não pode ser vazio");
		}

		if (technologyType == null || technologyName.isBlank()) {
			throw new ResourceBadRequestException("O tipo da tecnologia não pode ser vazio");
		}

		List<Technology> availableTechnologies = technologyRepository.findAll();
		for (Technology availableTechnology : availableTechnologies) {
			if(technologyName.equalsIgnoreCase(availableTechnology.getName())){
				throw new ResourceBadRequestException("A habilidade: '"+technologyName+"' já está cadastrada!");
			}
		}

		return true;
	}


}

package br.com.vivo.challengeform.dto;

import br.com.vivo.challengeform.enums.TechnologyTypes;
import br.com.vivo.challengeform.validator.ValidTechnology;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ValidTechnology
public class TechnologyDTO {
	private Long id;

	private String name;

	private TechnologyTypes type;

	@Override
	public String toString() {
		return this.name;
	}
}

package br.com.vivo.challengeform.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TechnologyDTO {
	private long id;

	private String name;

	private String type;
	
	private int rating;

}

package br.com.vivo.challengeform.dto;

import java.util.List;

import br.com.vivo.challengeform.enums.TechnologyTypes;
import br.com.vivo.challengeform.validator.ValidUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ValidUser
public class UserDTO {
	private long id;

	private String name;

	private String email;

	private TechnologyTypes profile = TechnologyTypes.NONE;

	private List<LevelDTO> levels;

	@Override
	public String toString() {
		return "UserDTO{" +
				"id=" + id +
				", name='" + name + '\'' +
				", email='" + email + '\'' +
				", profile=" + profile +
				", levels=" + levels +
				'}';
	}
}

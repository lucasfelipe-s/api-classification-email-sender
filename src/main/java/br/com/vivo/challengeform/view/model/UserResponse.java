package br.com.vivo.challengeform.view.model;

import br.com.vivo.challengeform.entities.Skill;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private long id;

    private String name;

    private String email;

    private List<SkillResponse> skills;

}

package br.com.vivo.challengeform.view.model;

import br.com.vivo.challengeform.enums.TechnologyTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TechnologyResponse {

    private long id;

    private String name;

    private TechnologyTypes type;

}

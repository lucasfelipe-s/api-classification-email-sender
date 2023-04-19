package br.com.vivo.challengeform.view.model;


import br.com.vivo.challengeform.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TechnologyRequest {

    private String name;

    private int rating;

    private List<UserDTO> users;
}

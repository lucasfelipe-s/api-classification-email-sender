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
public class TechnologyRequest {

    private String name;

    private TechnologyTypes type;

    @Override
    public String toString() {
        return "TechnologyRequest{" +
                "name='" + name + '\'' +
                ", type=" + type +
                '}';
    }
}

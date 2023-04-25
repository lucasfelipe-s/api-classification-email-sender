package br.com.vivo.challengeform.dto;

import br.com.vivo.challengeform.validator.ValidLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ValidLevel
public class LevelDTO {
    private long id;

    private int value;

    private TechnologyDTO technology;

    @Override
    public String toString() {
        return "LevelDTO{" +
                "id=" + id +
                ", value=" + value +
                ", technology=" + technology +
                '}';
    }
}

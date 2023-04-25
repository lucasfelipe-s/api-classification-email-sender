package br.com.vivo.challengeform.view.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LevelRequest {
    private int value;

    private UserRequest user;

    private TechnologyRequest technology;

    @Override
    public String toString() {
        return "LevelRequest{" +
                "value=" + value +
                ", technology=" + technology +
                '}';
    }
}

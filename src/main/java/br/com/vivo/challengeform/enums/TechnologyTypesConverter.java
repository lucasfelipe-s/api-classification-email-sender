package br.com.vivo.challengeform.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class TechnologyTypesConverter  implements AttributeConverter<TechnologyTypes, String> {

    @Override
    public String convertToDatabaseColumn(TechnologyTypes technologyTypes) {
        if(technologyTypes == null){
            return null;
        }
        return technologyTypes.getValue();
    }

    @Override
    public TechnologyTypes convertToEntityAttribute(String value) {
        if(value == null){
            return null;
        }
        return Stream.of(TechnologyTypes.values())
                .filter(c -> c.getValue()
                        .equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}

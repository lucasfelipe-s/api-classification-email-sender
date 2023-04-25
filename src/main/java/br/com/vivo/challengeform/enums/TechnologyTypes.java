package br.com.vivo.challengeform.enums;

public enum TechnologyTypes {
    FRONT_END("Front End"),
    BACK_END("Back End"),
    FULL_STACK("Full Stack"),
    NONE("Nenhum");

    private final String value;

    TechnologyTypes(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}

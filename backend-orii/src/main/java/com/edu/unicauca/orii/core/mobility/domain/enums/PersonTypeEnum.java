package com.edu.unicauca.orii.core.mobility.domain.enums;

public enum PersonTypeEnum {
    TEACHER("PROFESOR"), 
    STUDENT("ESTUDIANTE"),
    ADMIN("ADMINISTRATIVO");

    private final String displayName;

    PersonTypeEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}

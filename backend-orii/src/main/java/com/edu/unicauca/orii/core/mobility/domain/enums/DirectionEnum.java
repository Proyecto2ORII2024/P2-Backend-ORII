package com.edu.unicauca.orii.core.mobility.domain.enums;

public enum DirectionEnum {
    OUTGOING_IN_PERSON("SALIENTE PRESENCIAL"), 
    INCOMING_IN_PERSON("ENTRANTE PRESENCIAL"), 
    OUTGOING_VIRTUAL("SALIENTE VIRTUAL"), 
    INCOMING_VIRTUAL("ENTRANTE VIRTUAL");

    private final String displayName;

    DirectionEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}

package com.edu.unicauca.orii.core.mobility.domain.enums;

public enum ScopeEnum {
    NATIONAL("NACIONAL"), INTERNATIONAL("INTERNACIONAL");

    private final String displayName;

    ScopeEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}

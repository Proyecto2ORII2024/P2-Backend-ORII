package com.edu.unicauca.orii.core.common.domain.enums;

public enum FacultyEnum {
    FIET("Facultad de Ingeniería Electrónica y Telecomunicaciones"),
    FIC("Facultad de Ingeniería Civil"),
    FCS("Facultad de Ciencias de la Salud"),
    FDCPS("Facultad de Derecho y Ciencias Políticas y Sociales"),
    FACNED("Facultad de Ciencias Naturales, Exactas y de la Educación"),
    FCH("Facultad de Ciencias Humanas"),
    FA("Facultad de Artes"),
    FCA("Facultad de Ciencias Agropecuarias"),
    FCCEA("Facultad de Ciencias Contables, Económicas y Administrativas");

    private final String displayName;

    FacultyEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}

package com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.projection;

import com.edu.unicauca.orii.core.common.domain.enums.FacultyEnum;

public interface MobilityFacultyProjection {
    FacultyEnum getFaculty();
    Long getOutput();
    Long getInput();
}

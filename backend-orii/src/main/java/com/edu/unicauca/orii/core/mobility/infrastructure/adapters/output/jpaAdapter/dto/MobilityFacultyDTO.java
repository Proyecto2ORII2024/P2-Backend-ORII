package com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.dto;

import com.edu.unicauca.orii.core.common.domain.enums.FacultyEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MobilityFacultyDTO {
    private FacultyEnum faculty;
    private Long output;
    private Long input;
}

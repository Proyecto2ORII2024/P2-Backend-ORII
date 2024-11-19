package com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter;

import java.util.List;

import org.springframework.stereotype.Component;

import com.edu.unicauca.orii.core.mobility.application.ports.output.IStatisticsMobilityOutputPort;
import com.edu.unicauca.orii.core.mobility.domain.model.statistics.MobilityFaculty;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.projection.MobilityFacultyProjection;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.repository.IFormRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StatisticsMobilityJpaAdapter implements IStatisticsMobilityOutputPort {

    private final IFormRepository formRepository;

    @Override
    public MobilityFaculty getStatisticsByFaculty() {
        List<MobilityFacultyProjection> mobilityFacultyDTO = formRepository.getFacultyStatistics();

        List<String> facultyNames = mobilityFacultyDTO.stream()
            .map(stat -> stat.getFaculty().getDisplayName())
            .toList();
        
        List<Integer> input = mobilityFacultyDTO.stream()
            .map(dto -> dto.getInput().intValue())
            .toList();
        
        List<Integer> output = mobilityFacultyDTO.stream()
            .map(dto -> dto.getOutput().intValue())
            .toList();

        return MobilityFaculty.builder()
            .faculty(facultyNames)
            .input(input)
            .output(output)
            .build();   
    }
    
}

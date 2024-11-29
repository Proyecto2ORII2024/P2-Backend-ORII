package com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter;

import java.util.List;

import com.edu.unicauca.orii.core.mobility.domain.model.statistics.MobilityAgreementType;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.projection.MobilityAgreementTypeProjection;
import org.springframework.stereotype.Component;

import com.edu.unicauca.orii.core.mobility.application.ports.output.IStatisticsMobilityOutputPort;
import com.edu.unicauca.orii.core.mobility.domain.model.statistics.MobilityFaculty;
import com.edu.unicauca.orii.core.mobility.domain.model.statistics.MobilityTrend;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.projection.MobilityFacultyProjection;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.projection.MobilityTrendProjection;
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

    @Override
    public MobilityTrend getAnnualMobilityTrend() {
       List<MobilityTrendProjection> mobilityTrendDTO=this.formRepository.getAnnualMobilityTrend();

       List<Integer> years=mobilityTrendDTO.stream()
        .map(dto->dto.getYears().intValue())
        .toList();

        List<Integer> amountMobility=mobilityTrendDTO.stream()
        .map(dto->dto.getAmountMobility().intValue())
        .toList();

        return MobilityTrend.builder()
                .years(years)
                .amountMobility(amountMobility)
                .build();
    }

    @Override
    public MobilityAgreementType getDistributionByTypeOfAgreement() {
        List<MobilityAgreementTypeProjection> mobilityEventTypes = this.formRepository.getDistributionByTypeOfAgreement();

        List<String> agreementTypes = mobilityEventTypes.stream()
                .map(dto -> dto.getAgreementType())
                .toList();

        List<Integer> counts = mobilityEventTypes.stream()
                .map(dto -> dto.getTotalAgreementType().intValue())
                .toList();
        return MobilityAgreementType.builder()
                .agreementType(agreementTypes)
                .totalMobilityByAgreementsType(counts)
                .build();
    }


}

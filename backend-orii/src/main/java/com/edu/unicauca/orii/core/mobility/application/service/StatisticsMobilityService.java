package com.edu.unicauca.orii.core.mobility.application.service;

import com.edu.unicauca.orii.core.mobility.domain.model.statistics.MobilityAgreementType;
import org.springframework.stereotype.Service;

import com.edu.unicauca.orii.core.mobility.application.ports.input.IStatisticsMobilityPort;
import com.edu.unicauca.orii.core.mobility.application.ports.output.IStatisticsMobilityOutputPort;
import com.edu.unicauca.orii.core.mobility.domain.model.statistics.MobilityFaculty;
import com.edu.unicauca.orii.core.mobility.domain.model.statistics.MobilityTrend;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatisticsMobilityService implements IStatisticsMobilityPort {

    private final IStatisticsMobilityOutputPort statisticsMobilityOutputPort;   

    @Override
    public MobilityFaculty getStatisticsByFaculty() {
        return statisticsMobilityOutputPort.getStatisticsByFaculty();
    }

    @Override
    public MobilityTrend getAnnualMobilityTrend() {
      return statisticsMobilityOutputPort.getAnnualMobilityTrend();
    }

    @Override
    public MobilityAgreementType getDistributionByTypeOfAgreement() {
        return statisticsMobilityOutputPort.getDistributionByTypeOfAgreement();
    }


}

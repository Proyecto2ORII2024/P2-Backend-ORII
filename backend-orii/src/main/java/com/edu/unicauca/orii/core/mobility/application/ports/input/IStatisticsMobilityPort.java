package com.edu.unicauca.orii.core.mobility.application.ports.input;

import com.edu.unicauca.orii.core.mobility.domain.model.statistics.MobilityAgreementType;
import com.edu.unicauca.orii.core.mobility.domain.model.statistics.MobilityCountry;
import com.edu.unicauca.orii.core.mobility.domain.model.statistics.MobilityFaculty;
import com.edu.unicauca.orii.core.mobility.domain.model.statistics.MobilityTrend;

public interface IStatisticsMobilityPort {
    MobilityFaculty getStatisticsByFaculty();
    MobilityTrend getAnnualMobilityTrend();
    MobilityAgreementType getDistributionByTypeOfAgreement();
    MobilityCountry getStatisticsByCountry();
}

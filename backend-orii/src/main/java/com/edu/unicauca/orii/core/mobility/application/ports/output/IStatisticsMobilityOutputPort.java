package com.edu.unicauca.orii.core.mobility.application.ports.output;

import com.edu.unicauca.orii.core.mobility.domain.model.statistics.MobilityFaculty;

public interface IStatisticsMobilityOutputPort {
    MobilityFaculty getStatisticsByFaculty();
}

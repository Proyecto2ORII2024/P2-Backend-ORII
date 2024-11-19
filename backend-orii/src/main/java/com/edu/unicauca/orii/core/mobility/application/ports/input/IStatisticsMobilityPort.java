package com.edu.unicauca.orii.core.mobility.application.ports.input;

import com.edu.unicauca.orii.core.mobility.domain.model.statistics.MobilityFaculty;

public interface IStatisticsMobilityPort {
    MobilityFaculty getStatisticsByFaculty();
}

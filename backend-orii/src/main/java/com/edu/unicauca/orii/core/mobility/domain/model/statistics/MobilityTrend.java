package com.edu.unicauca.orii.core.mobility.domain.model.statistics;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MobilityTrend {
    List<Integer>years;
    List<Integer>amountMobility;
}

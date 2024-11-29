package com.edu.unicauca.orii.core.mobility.domain.model.statistics;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MobilityAgreementType {
    List<String> agreementType;
    List<Integer> totalMobilityByAgreementsType;
}

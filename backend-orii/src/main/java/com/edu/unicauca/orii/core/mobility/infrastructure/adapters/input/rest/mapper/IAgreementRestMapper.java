package com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.edu.unicauca.orii.core.mobility.domain.model.Agreement;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.data.AgreementData;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.data.response.AgreementSearchResponse;

@Mapper(componentModel = "spring")
public interface IAgreementRestMapper {

    @Mapping(target = "forms", ignore = true)
    Agreement toAgreement(AgreementData agreementData);

    AgreementData toAgreementData(Agreement agreement);
    
    AgreementSearchResponse toResponse(Agreement agreement);
    
     List<AgreementData> toListAgreementData(List<Agreement> agreement);

    
}

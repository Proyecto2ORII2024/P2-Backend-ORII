package com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.edu.unicauca.orii.core.mobility.domain.model.Agreement;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.entity.AgreementEntity;

@Mapper(componentModel = "spring")
public interface IAgreementAdapterMapper {
    
    @Mapping(target = "forms", ignore = true)
    Agreement toAgreement(AgreementEntity agreementEntity);

    @Mapping(target = "forms", ignore = true)
    AgreementEntity toAgreementEntity(Agreement agreement);

    List<Agreement> toAgreementList(List<AgreementEntity> agreementEntities);
}

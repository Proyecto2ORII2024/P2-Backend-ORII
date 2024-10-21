package com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.edu.unicauca.orii.core.mobility.domain.model.Form;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.data.request.FormCreateRequest;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.data.response.FormCreateResponse;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.input.rest.data.response.FormResponse;

@Mapper(componentModel = "spring", uses = {IPersonRestMapper.class})
public interface IFormRestMapper {

    FormCreateResponse toFormCreateResponse(Form form);

    @Mapping(target = "agreement.agreementId", source = "agreementId")
    @Mapping(target = "event.eventType.eventTypeId", source = "event.eventTypeId")  
    @Mapping(target = "event.forms", ignore = true) 
    Form toForm(FormCreateRequest formCreateRequest);

    FormResponse toFormResponse(Form form);

}

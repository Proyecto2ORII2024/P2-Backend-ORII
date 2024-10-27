package com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.edu.unicauca.orii.core.mobility.domain.model.Form;
import com.edu.unicauca.orii.core.mobility.infrastructure.adapters.output.jpaAdapter.entity.FormEntity;

@Mapper(componentModel = "spring")
public interface IFormAdapterMapper {

    @Mapping(target = "agreement", ignore = true)
    @Mapping(target = "event", ignore = true)
    @Mapping(target = "person", ignore = true)
    @Mapping(target = "user", ignore = true)
    FormEntity toFormEntity(Form form);

    @Mapping(target = "agreement.forms", ignore = true)
    @Mapping(target = "event.forms", ignore = true)
    @Mapping(target = "event.eventType.events", ignore = true)
    @Mapping(target = "person.forms", ignore = true)
    @Mapping(target = "user.forms", ignore = true)
    Form toForm(FormEntity formEntity);
  
}

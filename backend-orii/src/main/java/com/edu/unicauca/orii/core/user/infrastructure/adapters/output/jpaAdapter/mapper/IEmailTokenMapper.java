package com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.mapper;

import org.mapstruct.Mapper;

import com.edu.unicauca.orii.core.user.domain.model.EmailToken;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.output.jpaAdapter.entity.EmailTokenEntity;

@Mapper(componentModel = "spring")
public interface IEmailTokenMapper {
    EmailToken toDomain(EmailTokenEntity emailTokenEntity);
}

package com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.mapper;

import org.mapstruct.Mapper;

import com.edu.unicauca.orii.core.user.domain.model.MailData;
import com.edu.unicauca.orii.core.user.infrastructure.adapters.input.rest.data.request.MailRequest;

@Mapper(componentModel = "spring")
public interface IMailRestMapper {
    MailData toRequestMailData(MailRequest mailRequest);
}

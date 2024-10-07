package com.edu.unicauca.orii.core.mobility.application.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.edu.unicauca.orii.core.mobility.application.ports.input.IAgreementQueryPort;
import com.edu.unicauca.orii.core.mobility.application.ports.output.IAgreementQueryPersistencePort;
import com.edu.unicauca.orii.core.mobility.domain.model.Agreement;

import lombok.RequiredArgsConstructor;

/**
 * Implementación de servicio para la gestión de entidades {@link Agreement}.
 * <p>
 * Esta clase implementa la interfaz {@link IAgreementQueryPort} y
 * proporciona métodos
 * para listar entidades{@link Agreement} 
 * </p>
 * 
 * <p>
 * Utiliza el {@link IAgreementQueryPersistencePort} para poder listar los datos que estan guardados en la base de datos.

 * </p>
 * 
 * <p>
 * Esta clase está anotada con {@link Service} para marcarla como un servicio de Spring
 * y
 * {@link RequiredArgsConstructor} para generar automáticamente el constructor 
 * </p>
 * 
 * @autor brayan142002
 * 
 */
@Service
@RequiredArgsConstructor
public class AgreementQueryService implements IAgreementQueryPort {

    private final IAgreementQueryPersistencePort agreementQueryPersistencePort;
    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Agreement> getAgreement(Pageable pageable) {
        return agreementQueryPersistencePort.getAgreement(pageable);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Agreement> getAgreementByNumberOrName(String search) {
       return agreementQueryPersistencePort.getAgreementByNumberOrName(search);
    }
    
  
}
